package com.jakger.feature_maplibremaps.ui.main

import com.jakger.core_arch.mvi.MviReducer
import com.jakger.core_arch.mvi.UiEvent
import com.jakger.core_arch.mvi.UiSideEffect
import com.jakger.core_arch.mvi.UiState
import javax.inject.Inject

class MapLibreMapsReducer @Inject constructor() :
    MviReducer<UiState, UiEvent, UiSideEffect> {
    override fun invoke(
        event: UiEvent,
        state: UiState
    ): Pair<MapLibreUiState, MapLibreUiSideEffect> {
        TODO("Not yet implemented")
    }
}