package com.jakger.feature_maplibremaps.ui.main

import com.jakger.core_arch.mvi.UiEvent

sealed class MapLibreMapsUiEvent : UiEvent {
    data object zoomInClick : MapLibreMapsUiEvent()
    data object zoomOutClick : MapLibreMapsUiEvent()
    data object menuClick : MapLibreMapsUiEvent()
}
