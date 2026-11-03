package com.jakger.ghcrange_mobile.ui.main

import androidx.compose.runtime.Stable
import com.jakger.core_arch.mvi.UiState
import com.jakger.ghcrange_mobile.model.HealthRecord

sealed class GhcRangeUiState : UiState {

    @Stable
    data object Initial : GhcRangeUiState()

    @Stable
    data class Error(
        val message: String
    ) : GhcRangeUiState()

    @Stable
    data object Loading : GhcRangeUiState()

    @Stable
    data class View(
        val activityRecords: List<HealthRecord>,
        val vitalsRecords: List<HealthRecord>
    ) : GhcRangeUiState()
}

// Field: ActiveCaloriesBurnedRecord
// Field: DistanceRecord
