package com.jakger.googlemaps.ui.main

import com.jakger.core_arch.mvi.MviViewModel
import com.jakger.core_arch.mvi.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class GoogleMapsMainViewModel(
    override val reducer: GoogleMapsMainReducer,
) : MviViewModel(reducer) {

    private val _uiState: MutableStateFlow<GoogleMapsMainUiState> =
        MutableStateFlow(GoogleMapsMainUiState.Initial)
    private val _uiSideEffect = MutableSharedFlow<GoogleMapsMainUiSideEffect>(
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 1,
    )
    private val events = Channel<GoogleMapsMainUiEvent>()

    override val uiState: StateFlow<GoogleMapsMainUiState> = _uiState
    override val uiSideEffect: SharedFlow<GoogleMapsMainUiSideEffect> = _uiSideEffect

    override fun dispatchUiEvent(event: UiEvent) { /* no-op */ }
}