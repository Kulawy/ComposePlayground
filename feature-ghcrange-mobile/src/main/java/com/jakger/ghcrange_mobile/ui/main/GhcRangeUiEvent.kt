package com.jakger.ghcrange_mobile.ui.main

import com.jakger.core_arch.mvi.UiEvent
import com.jakger.feature_ghcrange.model.HeartRateAggregatedStats

sealed interface GhcRangeUiEvent : UiEvent {
    data object OnBackPressed : GhcRangeUiEvent
    data object CheckHealthDataAvailability : GhcRangeUiEvent
    data object HealthDataUnsupported : GhcRangeUiEvent
    data object HealthPermissionsDenied : GhcRangeUiEvent
    data object HealthPermissionsGranted : GhcRangeUiEvent

    data class HeartRateFetched(
        val heartRate: HeartRateAggregatedStats
    ) : GhcRangeUiEvent
}