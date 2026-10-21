package com.jakger.feature_maplibremaps.ui.main

import com.jakger.core_arch.mvi.UiSideEffect

sealed class MapLibreUiSideEffect : UiSideEffect {
    data object None : MapLibreUiSideEffect()
}