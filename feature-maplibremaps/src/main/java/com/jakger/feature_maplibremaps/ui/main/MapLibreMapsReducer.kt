package com.jakger.feature_maplibremaps.ui.main

import com.jakger.core_arch.mvi.MviReducer
import com.jakger.core_arch.mvi.UiEvent
import com.jakger.core_arch.mvi.UiSideEffect
import com.jakger.core_arch.mvi.UiState
import jakarta.inject.Inject

class MapLibreMapsReducer @Inject constructor() :
    MviReducer<UiState, UiEvent, UiSideEffect> {
    override fun invoke(
        event: UiEvent,
        state: UiState
    ): Pair<MapLibreUiState, MapLibreUiSideEffect> =
        when (state) {
            is MapLibreUiState.View -> state.reduce(event)
            else -> MapLibreUiState.Error to MapLibreUiSideEffect.None
        }

    private fun MapLibreUiState.View.reduce(
        event: UiEvent
    ): Pair<MapLibreUiState, MapLibreUiSideEffect> = when (event) {
        is MapLibreMapsUiEvent.menuClick -> copy(menuExpanded = !menuExpanded) to MapLibreUiSideEffect.None
        is MapLibreMapsUiEvent.zoomOutClick ->
            copy(zoom = if (zoom == 1.0) 1.0 else zoom - 1) to MapLibreUiSideEffect.None

        is MapLibreMapsUiEvent.zoomInClick -> copy(zoom = zoom + 1) to MapLibreUiSideEffect.None
        else -> this to MapLibreUiSideEffect.None
    }


}