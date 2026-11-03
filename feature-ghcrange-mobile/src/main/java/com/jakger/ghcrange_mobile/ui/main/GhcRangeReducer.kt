package com.jakger.ghcrange_mobile.ui.main

import com.jakger.core_arch.mvi.MviReducer
import com.jakger.core_arch.mvi.UiEvent
import com.jakger.core_arch.mvi.UiSideEffect
import com.jakger.core_arch.mvi.UiState
import jakarta.inject.Inject

class GhcRangeReducer @Inject constructor() :
    MviReducer<UiState, UiEvent, UiSideEffect> {
    override fun invoke(
        event: UiEvent,
        state: UiState
    ): Pair<GhcRangeUiState, GhcRangeUiSideEffect> {
        TODO("Not yet implemented")
    }
}

