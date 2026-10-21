package com.jakger.googlemaps.ui.main

import com.jakger.core_arch.mvi.UiEvent

sealed class GoogleMapsUiEvent : UiEvent {
    data object turnOn : GoogleMapsUiEvent()
}