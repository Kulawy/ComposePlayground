package com.jakger.googlemaps.ui.main

import com.jakger.core_arch.mvi.UiSideEffect

sealed class GoogleMapsMainUiSideEffect : UiSideEffect {
    data object None : GoogleMapsMainUiSideEffect()
}