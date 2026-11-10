package com.jakger.feature_ghcrange.model

import com.jakger.feature_ghcrange.model.ui.HeartRateAggregatedStatsUi

data class HeartRateAggregatedStats(
    val minBpm: Long?,
    val avgBpm: Long?,
    val maxBpm: Long?,
    val measurementsCount: Long,
)

fun HeartRateAggregatedStats.toUi(): HeartRateAggregatedStatsUi = HeartRateAggregatedStatsUi(
    minBpm = minBpm?.toString() ?: "?",
    avgBpm = avgBpm?.toString() ?: "?",
    maxBpm = maxBpm?.toString() ?: "?",
    measurementsCount = measurementsCount.toString()
)

fun HeartRateAggregatedStatsUi.toDomain(): HeartRateAggregatedStats = HeartRateAggregatedStats(
    minBpm = minBpm.toLongOrNull(),
    avgBpm = avgBpm.toLongOrNull(),
    maxBpm = maxBpm.toLongOrNull(),
    measurementsCount = measurementsCount.toLongOrNull() ?: 0
)