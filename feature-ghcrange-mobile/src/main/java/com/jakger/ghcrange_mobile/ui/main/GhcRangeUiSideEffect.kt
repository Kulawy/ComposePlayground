package com.jakger.ghcrange_mobile.ui.main

import com.jakger.core_arch.mvi.UiSideEffect

sealed interface GhcRangeUiSideEffect : UiSideEffect {
    data object FetchHealthData : GhcRangeUiSideEffect
    data object CheckHealthDataPermissions : GhcRangeUiSideEffect
    data object CheckHealthDataAvailability : GhcRangeUiSideEffect
    data object None : GhcRangeUiSideEffect

    sealed interface Navigation : GhcRangeUiSideEffect {
        data object Back : Navigation
    }

    sealed interface UiEffect : GhcRangeUiSideEffect {
        data object ExplainPermissions : UiEffect
        data object RequestPermissions : UiEffect
        data object OpenPermissionSettings : UiEffect
    }
}
