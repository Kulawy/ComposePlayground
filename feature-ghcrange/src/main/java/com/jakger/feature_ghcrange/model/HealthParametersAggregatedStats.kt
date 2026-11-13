package com.jakger.feature_ghcrange.model

import com.jakger.feature_ghcrange.model.ui.HealthParametersAggregatedStatsUi
import com.jakger.feature_ghcrange.toDisplayString

data class HealthParametersAggregatedStats(
    val floors: Double?,
    val distanceMeters: Double?,
    val steps: Long?,
    val activeCaloriesBurnedKcal: Double?,
    val totalCaloriesBurnedKcal: Double?,
)

fun HealthParametersAggregatedStats.toUi(): HealthParametersAggregatedStatsUi =
    HealthParametersAggregatedStatsUi(
        floors = floors?.toDisplayString(),
        distance = distanceMeters?.toDisplayString(),
        steps = steps?.toString(),
        activeCaloriesBurned = activeCaloriesBurnedKcal?.toDisplayString(),
        totalCaloriesBurned = totalCaloriesBurnedKcal?.toDisplayString(),
        systolicAverageBloodPressure = "?",
    )
