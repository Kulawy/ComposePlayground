package com.jakger.googlemaps.ui

import com.jakger.core_arch.mvi.UiSideEffect

sealed class GoogleMapsMainUiSideEffect : UiSideEffect {
    data object None : GoogleMapsMainUiSideEffect()
}