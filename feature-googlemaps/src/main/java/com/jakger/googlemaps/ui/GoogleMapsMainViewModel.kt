package com.jakger.googlemaps.ui

import com.jakger.core_arch.mvi.MviReducer
import com.jakger.core_arch.mvi.MviViewModel
import com.jakger.core_arch.mvi.UiEvent
import com.jakger.core_arch.mvi.UiSideEffect
import com.jakger.core_arch.mvi.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class GoogleMapsMainViewModel(
    override val reducer: MviReducer<UiState, UiEvent, UiSideEffect>,
) : MviViewModel(reducer) {

    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(GoogleMapsMainUiState.Initial)
    private val _uiSideEffect = MutableSharedFlow<UiSideEffect>(
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 1,
    )
    private val events = Channel<GoogleMapsMainUiEvent>()

    override val uiState: StateFlow<UiState> = _uiState
    override val uiSideEffect: SharedFlow<UiSideEffect> = _uiSideEffect
//    override val uiEffect: SharedFlow<UiEvent> = _uiEffect

    override fun dispatchUiEvent(event: UiEvent) {
    }
}