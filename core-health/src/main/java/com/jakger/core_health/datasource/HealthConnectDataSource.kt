package com.jakger.core_health.datasource

import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.HeartRateRecord
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
    private val healthConnectClient: HealthConnectClient,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

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
}