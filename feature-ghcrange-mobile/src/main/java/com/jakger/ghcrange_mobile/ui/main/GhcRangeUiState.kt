package com.jakger.ghcrange_mobile.ui.main

import androidx.compose.runtime.Stable
import com.jakger.core_arch.mvi.UiState

sealed class GhcRangeUiState : UiState {

    @Stable
    data object Initial : GhcRangeUiState()

    @Stable
    data class Error(
        val message: String
    ) : GhcRangeUiState()

    @Stable
    data class Loading(
        val message: String
    ) : GhcRangeUiState()

}