package com.jakger.core_health.datasource

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.HealthConnectFeatures
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.RestingHeartRateRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.jakger.core_arch.di.IoDispatcher
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.time.Instant

/***
 * Health Connect Data Source
 * as Google Health Connect not updates data in real time,
 * we only use suspend function as 'one shot' call for data
 */
@Singleton
class HealthConnectDataSource @Inject constructor(
    context: Context,
//    private val healthConnectClient: HealthConnectClient,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    private val appContext = context.applicationContext
    private val healthConnectClient: HealthConnectClient by lazy {
        HealthConnectClient.getOrCreate(
            appContext
        )
    }

    fun sdkStatus(): Int = HealthConnectClient.getSdkStatus(appContext)

    fun isHistoryReadSupported(): Boolean =
        healthConnectClient.features.getFeatureStatus(HealthConnectFeatures.FEATURE_READ_HEALTH_DATA_HISTORY) ==
            HealthConnectFeatures.FEATURE_STATUS_AVAILABLE

    suspend fun getGrantedPermissions() =
        healthConnectClient.permissionController.getGrantedPermissions()

    suspend fun readHeartRate(start: Instant, end: Instant): List<HeartRateRecord> =
        withContext(ioDispatcher) {
            val records = mutableListOf<HeartRateRecord>()
            var pageToken: String? = null
            do {
                val response = healthConnectClient.readRecords(
                    ReadRecordsRequest(
                        recordType = HeartRateRecord::class,
                        timeRangeFilter = TimeRangeFilter.between(start, end),
                        pageSize = 1000,
                        pageToken = pageToken,
                    )
                )
                records += response.records
                pageToken = response.pageToken
            } while (pageToken != null)
            records
        }

    /** Resting heart rate has one measurement per record and no aggregate count metric. */
    suspend fun countRestingHeartRate(start: Instant, end: Instant): Long =
        withContext(ioDispatcher) {
            var count = 0L
            var pageToken: String? = null
            do {
                val response = healthConnectClient.readRecords(
                    ReadRecordsRequest(
                        recordType = RestingHeartRateRecord::class,
                        timeRangeFilter = TimeRangeFilter.between(start, end),
                        pageSize = 1000,
                        pageToken = pageToken,
                    )
                )
                count += response.records.size
                pageToken = response.pageToken?.takeIf { it.isNotEmpty() }
            } while (pageToken != null)
            count
        }
}
