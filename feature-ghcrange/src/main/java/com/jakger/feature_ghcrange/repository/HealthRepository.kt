package com.jakger.feature_ghcrange.repository

//import com.jakger.feature_ghcrange.model.HealthConnectResult
import com.jakger.core_common.domain.DomainResult
import com.jakger.feature_ghcrange.model.HeartRateSample
import kotlinx.coroutines.flow.Flow
import java.time.Instant

interface HealthRepository {
    fun observeSteps(): Flow<Int>
    fun observeCaloriesBurned(): Flow<Int>
    suspend fun checkPermissions(): Boolean
    suspend fun getStepsForPeriod(
        startTime: Instant,
        endTime: Instant
    ): DomainResult<Long>

    suspend fun getHeartRateForPeriod(
        startTime: Instant,
        endTime: Instant
    ): DomainResult<List<HeartRateSample>>
}