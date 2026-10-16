package com.jakger.googlemaps.ui

import androidx.compose.runtime.Stable
import com.jakger.core_arch.mvi.UiState

sealed class GoogleMapsMainUiState : UiState {

    @Stable
    data object Initial : GoogleMapsMainUiState()

    @Stable
    data class Error(
        val message: String
    ) : GoogleMapsMainUiState()

    @Stable
    data class Loading(
        val message: String
    ) : GoogleMapsMainUiState()

}