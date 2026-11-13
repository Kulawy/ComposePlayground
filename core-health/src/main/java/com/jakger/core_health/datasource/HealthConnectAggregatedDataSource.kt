package com.jakger.core_health.datasource

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.FloorsClimbedRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.RestingHeartRateRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.jakger.core_arch.di.IoDispatcher
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import java.time.Instant

/***
 * Health Connect Aggregated Data Source
 * as Google Health Connect not updates data in real time,
 * we only use suspend function as 'one shot' call for data
 */
@Singleton
class HealthConnectAggregatedDataSource @Inject constructor(
//    private val healthConnectClient: HealthConnectClient,
    context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    private val appContext = context.applicationContext
    private val healthConnectClient: HealthConnectClient by lazy {
        HealthConnectClient.getOrCreate(
            appContext
        )
    }

    // ACTIVITY & EXERCISED
    suspend fun getStepsForPeriod(startTime: Instant, endTime: Instant): Long? {
        val response = healthConnectClient.aggregate(
            AggregateRequest(
                metrics = setOf(StepsRecord.COUNT_TOTAL),
                timeRangeFilter = TimeRangeFilter.between(
                    startTime = startTime,
                    endTime = endTime
                )
            )
        )

        return response[StepsRecord.COUNT_TOTAL]
    }

    suspend fun getDistanceRecord(startTime: Instant, endTime: Instant) =
        healthConnectClient.aggregate(
            AggregateRequest(
                metrics = setOf(DistanceRecord.DISTANCE_TOTAL),
                timeRangeFilter = TimeRangeFilter.between(
                    startTime = startTime,
                    endTime = endTime
                )
            )
        )

    suspend fun getFloorsClimbedRecord(startTime: Instant, endTime: Instant) =
        healthConnectClient.aggregate(
            AggregateRequest(
                metrics = setOf(FloorsClimbedRecord.FLOORS_CLIMBED_TOTAL),
                timeRangeFilter = TimeRangeFilter.between(
                    startTime = startTime,
                    endTime = endTime
                )
            )
        )

    suspend fun getActiveCaloriesBurned(startTime: Instant, endTime: Instant) =
        healthConnectClient.aggregate(
            AggregateRequest(
                metrics = setOf(ActiveCaloriesBurnedRecord.ACTIVE_CALORIES_TOTAL),
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime),
            )
        )[ActiveCaloriesBurnedRecord.ACTIVE_CALORIES_TOTAL]?.inKilocalories

    suspend fun getTotalCaloriesBurned(startTime: Instant, endTime: Instant) =
        healthConnectClient.aggregate(
            AggregateRequest(
                metrics = setOf(TotalCaloriesBurnedRecord.ENERGY_TOTAL),
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime),
            )
        )[TotalCaloriesBurnedRecord.ENERGY_TOTAL]?.inKilocalories

    // VITALS

    suspend fun getSystolicAverageBloodPressure(startTime: Instant, endTime: Instant) =
        healthConnectClient.aggregate(
            AggregateRequest(
                metrics = setOf(BloodPressureRecord.SYSTOLIC_AVG),
                timeRangeFilter = TimeRangeFilter.between(startTime, Instant.now()),
            )
        )

    suspend fun getHearRate(startTime: Instant, endTime: Instant) =
        healthConnectClient.aggregate(
            AggregateRequest(
                metrics = setOf(
                    HeartRateRecord.BPM_MIN,
                    HeartRateRecord.BPM_AVG,
                    HeartRateRecord.BPM_MAX,
                    HeartRateRecord.MEASUREMENTS_COUNT,
                ),
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime),
            )
        )

    suspend fun getRestingHeartRate(startTime: Instant, endTime: Instant) =
        healthConnectClient.aggregate(
            AggregateRequest(
                metrics = setOf(
                    RestingHeartRateRecord.BPM_MIN,
                    RestingHeartRateRecord.BPM_AVG,
                    RestingHeartRateRecord.BPM_MAX,
                ),
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime),
            )
        )
}
