package com.jakger.ghcrange_mobile.ui.main

import androidx.compose.runtime.Stable
import com.jakger.core_arch.mvi.UiState
import com.jakger.feature_ghcrange.model.HeartRateAggregatedStats
import com.jakger.ghcrange_mobile.model.HealthRecord

sealed interface GhcRangeUiState : UiState {

    @Stable
    data object Initial : GhcRangeUiState

    @Stable
    sealed interface Error: GhcRangeUiState {
        data object PermissionsNotGranted: Error
        data object HealthDataNotAvailable: Error
    }

    @Stable
    data object Loading : GhcRangeUiState

    @Stable
    data class Display(
        val aggregatedHeartRate: HeartRateAggregatedStats,
//        val activityRecords: List<HealthRecord>,
//        val vitalsRecords: List<HealthRecord>
    ) : GhcRangeUiState
}

// Field: ActiveCaloriesBurnedRecord
// Field: DistanceRecord
