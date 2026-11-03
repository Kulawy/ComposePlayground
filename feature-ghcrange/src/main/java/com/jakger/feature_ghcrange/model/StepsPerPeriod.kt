package com.jakger.feature_ghcrange.model

import java.time.Instant

data class StepsPerPeriod(
    val count: Long,
    val startTime: Instant,
    val endTime: Instant
)