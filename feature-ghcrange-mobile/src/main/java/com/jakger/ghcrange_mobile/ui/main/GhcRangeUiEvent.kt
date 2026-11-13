package com.jakger.ghcrange_mobile.ui.main

import com.jakger.core_arch.mvi.UiEvent
import com.jakger.feature_ghcrange.model.HealthParametersAggregatedStats
import com.jakger.feature_ghcrange.model.HeartRateAggregatedStats

sealed interface GhcRangeUiEvent : UiEvent {
    data object OnBackPressed : GhcRangeUiEvent
    data object CheckHealthDataAvailability : GhcRangeUiEvent
    data object HealthDataUnsupported : GhcRangeUiEvent
    data object HealthPermissionsDenied : GhcRangeUiEvent
    data object HealthPermissionsGranted : GhcRangeUiEvent
    data object RequestPermission : GhcRangeUiEvent
    data object HealthPermissionsRequired : GhcRangeUiEvent
    data object ManagePermissions : GhcRangeUiEvent
    data object ScreenResumed : GhcRangeUiEvent
    data object PermissionSettingsClosed : GhcRangeUiEvent
    data class PermissionUiEffectLaunched(val effect: GhcRangeUiSideEffect.UiEffect) : GhcRangeUiEvent
    data object PermissionUiLaunchFailed : GhcRangeUiEvent
    data class HealthDataFetched(
        val yesterdayHeartRate: HeartRateAggregatedStats,
        val lastYearHeartRate: HeartRateAggregatedStats,
        val yesterdayRestingHeartRate: HeartRateAggregatedStats,
        val lastYearRestingHeartRate: HeartRateAggregatedStats,
        val healthParametersYesterday: HealthParametersAggregatedStats,
        val healthParametersLastYear: HealthParametersAggregatedStats,
    ) : GhcRangeUiEvent
}
