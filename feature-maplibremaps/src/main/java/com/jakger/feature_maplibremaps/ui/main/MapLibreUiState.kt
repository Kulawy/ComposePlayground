package com.jakger.feature_maplibremaps.ui.main

import com.jakger.core_arch.mvi.UiState

sealed class MapLibreUiState : UiState {
    data object Loading : MapLibreUiState()
    data object Error : MapLibreUiState()
}
