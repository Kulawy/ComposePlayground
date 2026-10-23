package com.jakger.feature_maplibremaps.ui.main

import androidx.lifecycle.viewModelScope
import com.jakger.core_arch.mvi.MviViewModel
import com.jakger.core_arch.mvi.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapLibreViewModel @Inject constructor(
    override val reducer: MapLibreMapsReducer
) : MviViewModel(reducer) {

    private val _uiState: MutableStateFlow<MapLibreUiState> =
        MutableStateFlow(MapLibreUiState.View())
    private val _uiSideEffect = MutableSharedFlow<MapLibreUiSideEffect>(
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 1,
    )
    private val events = Channel<MapLibreMapsUiEvent>()

    override val uiState: StateFlow<MapLibreUiState> = _uiState
    override val uiSideEffect: SharedFlow<MapLibreUiSideEffect> = _uiSideEffect

    override fun dispatchUiEvent(event: UiEvent) {
        if (event is MapLibreMapsUiEvent) {
            viewModelScope.launch {
                events.send(event)
            }
        }
    }

    init {
        consumeEvent()
//        consumeSideEffect()
    }

    private fun consumeEvent() {
        viewModelScope.launch {
            events.consumeEach { event ->
                reducer(event, uiState.value).let { (state, effect) ->
                    _uiState.emit(state)
                    _uiSideEffect.emit(effect)
                }
            }
        }
    }
}