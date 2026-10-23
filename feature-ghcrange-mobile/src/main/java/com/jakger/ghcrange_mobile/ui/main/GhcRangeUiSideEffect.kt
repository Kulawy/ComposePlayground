package com.jakger.ghcrange_mobile.ui.main

import com.jakger.core_arch.mvi.UiSideEffect

sealed class GhcRangeUiSideEffect : UiSideEffect {
    data object None : GhcRangeUiSideEffect()
}