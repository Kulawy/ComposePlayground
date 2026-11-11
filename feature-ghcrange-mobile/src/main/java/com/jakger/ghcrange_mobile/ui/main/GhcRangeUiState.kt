package com.jakger.ghcrange_mobile.ui.main

import androidx.compose.runtime.Stable
import com.jakger.core_arch.mvi.UiState
import com.jakger.feature_ghcrange.model.ui.HeartRateAggregatedStatsUi

sealed interface GhcRangeUiState : UiState {

    @Stable
    data object Initial : GhcRangeUiState

    data class AwaitingPermissionExplanation(val launchPending: Boolean = true) : GhcRangeUiState
    data class AwaitingPermissionResult(val launchPending: Boolean = true) : GhcRangeUiState
    data class AwaitingPermissionSettings(val launchPending: Boolean = true) : GhcRangeUiState

    @Stable
    sealed interface Error : GhcRangeUiState {
        data object PermissionsNotGranted : Error
        data object HealthDataNotAvailable : Error
    }

    @Stable
    data object Loading : GhcRangeUiState

    @Stable
    data class Display(
        val aggregatedYesterdayHeartRate: HeartRateAggregatedStatsUi,
        val aggregatedLastYearHeartRate: HeartRateAggregatedStatsUi,
        val aggregatedYesterdayRestingHeartRate: HeartRateAggregatedStatsUi,
        val aggregatedLastYearRestingHeartRate: HeartRateAggregatedStatsUi,
    ) : GhcRangeUiState
}

// Field: ActiveCaloriesBurnedRecord
// Field: DistanceRecord
