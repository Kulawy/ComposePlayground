package com.jakger.feature_ghcrange.model

data class HeartRateAggregatedStats(
    val minBpm: Long?,
    val avgBpm: Long?,
    val maxBpm: Long?,
    val measurementsCount: Long,
)