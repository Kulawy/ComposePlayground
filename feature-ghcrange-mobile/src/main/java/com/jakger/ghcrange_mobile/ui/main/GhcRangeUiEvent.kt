package com.jakger.ghcrange_mobile.ui.main

import com.jakger.core_arch.mvi.UiEvent

sealed class GhcRangeUiEvent : UiEvent {
    data object turnOn : GhcRangeUiEvent()
}