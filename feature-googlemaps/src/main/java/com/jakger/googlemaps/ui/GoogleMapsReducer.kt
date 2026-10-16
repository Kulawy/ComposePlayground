package com.jakger.googlemaps.ui

import com.jakger.core_arch.mvi.MviReducer
import javax.inject.Inject

class GoogleMapsMainReducer @Inject constructor() :
    MviReducer<GoogleMapsMainUiState, GoogleMapsMainUiEvent, GoogleMapsMainUiSideEffect> {
    override fun invoke(
        event: GoogleMapsMainUiEvent,
        state: GoogleMapsMainUiState
    ): Pair<GoogleMapsMainUiState, GoogleMapsMainUiSideEffect> {
        TODO("Not yet implemented")
    }
}

