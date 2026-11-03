package com.jakger.ghcrange_mobile.ui.main

import com.jakger.core_arch.mvi.MviViewModel
import com.jakger.core_arch.mvi.UiEvent
import com.jakger.feature_ghcrange.usecase.GetStepsForTodayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import jakarta.inject.Inject

@HiltViewModel
class GhcRangeViewModel @Inject constructor(
    override val reducer: GhcRangeReducer,
    val getStepsForTodayUseCase: GetStepsForTodayUseCase
) : MviViewModel(reducer) {

    private val _uiState: MutableStateFlow<GhcRangeUiState> =
        MutableStateFlow(GhcRangeUiState.Initial)
    private val _uiSideEffect = MutableSharedFlow<GhcRangeUiSideEffect>(
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 1,
    )
    private val events = Channel<GhcRangeUiEvent>()

    override val uiState: StateFlow<GhcRangeUiState> = _uiState
    override val uiSideEffect: SharedFlow<GhcRangeUiSideEffect> = _uiSideEffect

    override fun dispatchUiEvent(event: UiEvent) { /* no-op */ }
}