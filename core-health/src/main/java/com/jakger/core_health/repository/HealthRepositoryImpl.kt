package com.jakger.core_health.repository

import android.os.RemoteException
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.StepsRecord
import com.jakger.core_arch.di.IoDispatcher
import com.jakger.core_common.domain.DomainError
import com.jakger.core_common.domain.DomainResult
import com.jakger.core_health.datasource.HealthConnectAggregatedDataSource
import com.jakger.core_health.datasource.HealthConnectDataSource
import com.jakger.feature_ghcrange.repository.HealthRepository
import com.jakger.feature_ghcrange.model.HealthConnectError
import com.jakger.feature_ghcrange.model.HeartRateSample
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import java.io.IOException
import java.time.Instant
import kotlin.coroutines.cancellation.CancellationException

class HealthConnectSdkRepositoryImpl @Inject constructor(
    private val healthDataSource: HealthConnectDataSource,
    private val healthAggregatedDataSource: HealthConnectAggregatedDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : HealthRepository {

    private val requiredPermissions = setOf(
        HealthPermission.getReadPermission(StepsRecord::class)
    )

    override fun observeSteps(): Flow<Int> {
        return flowOf(1)
    }

    override fun observeCaloriesBurned(): Flow<Int> {
        return flowOf(1)
    }

    override suspend fun checkPermissions(): Boolean {
        return healthDataSource.getGrantedPermissions().containsAll(requiredPermissions)
    }

    override suspend fun getStepsForPeriod(
        startTime: Instant,
        endTime: Instant
    ): DomainResult<Long> = withContext(ioDispatcher) {
        safeCall {
            healthAggregatedDataSource.getAggregatedSteps(startTime, endTime)
                ?: throw NoSuchElementException()
        }
    }

    override suspend fun getHeartRateForPeriod(
        startTime: Instant,
        endTime: Instant
    ): DomainResult<List<HeartRateSample>> = withContext(ioDispatcher) {
        safeCall {
            healthDataSource.readHeartRate(startTime, endTime)
                .flatMap { record -> record.samples }
                .map { sample -> HeartRateSample(time = sample.time, bpm = sample.beatsPerMinute) }
                .sortedBy { it.time }
        }
    }

    private suspend fun <T> safeCall(block: suspend () -> T): DomainResult<T> =
        try {
            DomainResult.Success(block())
        } catch (exception: CancellationException) {
            throw exception // must come first: CancellationException is an IllegalStateException
        } catch (exception: SecurityException) {
            DomainResult.Failure(HealthConnectError.PermissionDenied)
        } catch (exception: RemoteException) {
            DomainResult.Failure(HealthConnectError.ServiceUnavailable)
        } catch (exception: IOException) {
            DomainResult.Failure(HealthConnectError.ServiceUnavailable)
        } catch (exception: IllegalStateException) {
            DomainResult.Failure(HealthConnectError.ServiceUnavailable)
        } catch (exception: Exception) {
            DomainResult.Failure(DomainError.Unknown(exception.message))
        }
}