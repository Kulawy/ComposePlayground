package com.jakger.feature_ghcrange.model

import java.time.Instant

data class HeartRateSample(
    val time: Instant,
    val bpm: Long,
)