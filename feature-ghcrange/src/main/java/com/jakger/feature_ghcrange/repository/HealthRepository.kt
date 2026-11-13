package com.jakger.feature_ghcrange.repository

import com.jakger.core_common.domain.DomainResult
import com.jakger.feature_ghcrange.model.HealthAvailability
import com.jakger.feature_ghcrange.model.HealthConnectAccess
import com.jakger.feature_ghcrange.model.HeartRateAggregatedStats
import com.jakger.feature_ghcrange.model.HeartRateSample
import java.time.Instant

interface HealthRepository {
    fun availability(): HealthAvailability
    suspend fun checkPermissions(): Boolean
    suspend fun checkHistoryAccess(): HealthConnectAccess
    suspend fun getStepsForPeriod(
        startTime: Instant,
        endTime: Instant
    ): DomainResult<Long?>

    /** Total active energy in kilocalories. */
    suspend fun getActiveCaloriesBurnedForPeriod(
        startTime: Instant,
        endTime: Instant,
    ): DomainResult<Double?>

    /** Total energy in kilocalories, including active and basal energy. */
    suspend fun getTotalCaloriesBurnedForPeriod(
        startTime: Instant,
        endTime: Instant,
    ): DomainResult<Double?>

    /** Total distance in metres. */
    suspend fun getDistanceForPeriod(
        startTime: Instant,
        endTime: Instant,
    ): DomainResult<Double?>

    suspend fun getFloorsClimbedForPeriod(
        startTime: Instant,
        endTime: Instant,
    ): DomainResult<Double?>

    suspend fun getHeartRateForPeriod(
        startTime: Instant,
        endTime: Instant
    ): DomainResult<List<HeartRateSample>>

    suspend fun getAggregatedHeartRateForPeriod(
        startTime: Instant,
        endTime: Instant
    ): DomainResult<HeartRateAggregatedStats>

    suspend fun getAggregatedRestingHeartRateForPeriod(
        startTime: Instant,
        endTime: Instant,
    ): DomainResult<HeartRateAggregatedStats>
}
