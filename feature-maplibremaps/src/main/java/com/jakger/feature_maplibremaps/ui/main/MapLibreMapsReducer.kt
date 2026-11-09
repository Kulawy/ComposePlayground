package com.jakger.feature_maplibremaps.ui.main

import com.jakger.core_arch.mvi.MviReducer
import jakarta.inject.Inject

class MapLibreMapsReducer @Inject constructor() :
    MviReducer<MapLibreUiState, MapLibreMapsUiEvent, MapLibreUiSideEffect> {
    override fun invoke(
        event: MapLibreMapsUiEvent,
        state: MapLibreUiState
    ): Pair<MapLibreUiState, MapLibreUiSideEffect> =
        when (state) {
            is MapLibreUiState.View -> state.reduce(event)
            else -> MapLibreUiState.Error to MapLibreUiSideEffect.None
        }

    private fun MapLibreUiState.View.reduce(
        event: MapLibreMapsUiEvent
    ): Pair<MapLibreUiState, MapLibreUiSideEffect> = when (event) {
        is MapLibreMapsUiEvent.menuClick -> copy(menuExpanded = !menuExpanded) to MapLibreUiSideEffect.None
        is MapLibreMapsUiEvent.zoomOutClick ->
            copy(zoom = if (zoom == 1.0) 1.0 else zoom - 1) to MapLibreUiSideEffect.None

        is MapLibreMapsUiEvent.zoomInClick -> copy(zoom = zoom + 1) to MapLibreUiSideEffect.None
    }


}
