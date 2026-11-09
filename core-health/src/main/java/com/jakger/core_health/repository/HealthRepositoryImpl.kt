package com.jakger.core_health.repository

import android.os.RemoteException
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.StepsRecord
import com.jakger.core_arch.di.IoDispatcher
import com.jakger.core_common.domain.DomainError
import com.jakger.core_common.domain.DomainResult
import com.jakger.core_health.datasource.HealthConnectAggregatedDataSource
import com.jakger.core_health.datasource.HealthConnectDataSource
import com.jakger.feature_ghcrange.model.HealthAvailability
import com.jakger.feature_ghcrange.model.HealthConnectError
import com.jakger.feature_ghcrange.model.HeartRateAggregatedStats
import com.jakger.feature_ghcrange.model.HeartRateSample
import com.jakger.feature_ghcrange.repository.HealthRepository
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.IOException
import java.time.Instant
import kotlin.coroutines.cancellation.CancellationException

val healthPermissions = setOf(
    HealthPermission.getReadPermission(StepsRecord::class),
    HealthPermission.getReadPermission(HeartRateRecord::class),
    HealthPermission.getReadPermission(BloodPressureRecord::class),
    HealthPermission.getReadPermission(DistanceRecord::class),
    HealthPermission.getReadPermission(ActiveCaloriesBurnedRecord::class)
)

class HealthConnectSdkRepositoryImpl @Inject constructor(
    private val healthDataSource: HealthConnectDataSource,
    private val healthAggregatedDataSource: HealthConnectAggregatedDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : HealthRepository {

    override fun availability(): HealthAvailability =
        when (healthDataSource.sdkStatus()) {
            HealthConnectClient.SDK_AVAILABLE -> HealthAvailability.AVAILABLE
            HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED ->
                HealthAvailability.NEEDS_INSTALL_OR_UPDATE
            else -> HealthAvailability.UNSUPPORTED
        }

    override suspend fun checkPermissions(): Boolean {
        return healthDataSource.getGrantedPermissions().containsAll(healthPermissions)
    }

    override suspend fun getStepsForPeriod(
        startTime: Instant,
        endTime: Instant
    ): DomainResult<Long> = withContext(ioDispatcher) {
        safeCall {
            healthAggregatedDataSource.getStepsForPeriod(startTime, endTime)
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

    override suspend fun getAggregatedHeartRateForPeriod(
        startTime: Instant,
        endTime: Instant
    ): DomainResult<HeartRateAggregatedStats> = withContext(ioDispatcher) {
        safeCall {
            val result = healthAggregatedDataSource.getHearRate(startTime, endTime)
            HeartRateAggregatedStats(
                minBpm = result[HeartRateRecord.BPM_MIN],
                avgBpm = result[HeartRateRecord.BPM_AVG],
                maxBpm = result[HeartRateRecord.BPM_MAX],
                measurementsCount = result[HeartRateRecord.MEASUREMENTS_COUNT] ?: 0
            )
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