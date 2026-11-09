package com.jakger.feature_ghcrange.usecase

import com.jakger.core_common.domain.DomainResult
import com.jakger.core_common.logging.AppLog
import com.jakger.core_common.logging.LogLevel
import com.jakger.core_common.logging.LogSink
import com.jakger.feature_ghcrange.model.HealthAvailability
import com.jakger.feature_ghcrange.model.HealthConnectAccess
import com.jakger.feature_ghcrange.model.HeartRateAggregatedStats
import com.jakger.feature_ghcrange.model.HeartRateSample
import com.jakger.feature_ghcrange.repository.HealthRepository
import java.time.Instant
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CheckHealthConnectAvailabilityLoggingTest {
    private val entries = mutableListOf<Pair<String, String>>()

    @Before
    fun setUp() {
        AppLog.configure(LogLevel.DEBUG, LogSink { _, tag, message, _ -> entries += tag to message })
    }

    @After
    fun tearDown() {
        AppLog.disable()
    }

    @Test
    fun `domain use case uses its own class tag without Android`() = runBlocking {
        val useCase = CheckHealthConnectAvailabilityUseCase(Repository(HealthAvailability.AVAILABLE, true))
        assertEquals(HealthConnectAccess.Granted, useCase())
        assertEquals(
            listOf("CheckHealthConnectAvailabilityUseCase" to "Checking Health Connect access"),
            entries,
        )
    }

    @Test
    fun `logging preserves the permission decision`() = runBlocking {
        val useCase = CheckHealthConnectAvailabilityUseCase(Repository(HealthAvailability.AVAILABLE, false))
        assertEquals(HealthConnectAccess.NeedsPermission, useCase())
        assertEquals("CheckHealthConnectAvailabilityUseCase", entries.single().first)
    }

    @Test
    fun `disabled logging preserves the availability decision`() = runBlocking {
        AppLog.disable()
        val useCase = CheckHealthConnectAvailabilityUseCase(Repository(HealthAvailability.UNSUPPORTED, false))
        assertEquals(HealthConnectAccess.Unsupported, useCase())
        assertTrue(entries.isEmpty())
    }

    private class Repository(
        private val availability: HealthAvailability,
        private val permissions: Boolean,
    ) : HealthRepository {
        override fun availability(): HealthAvailability = availability
        override suspend fun checkPermissions(): Boolean = permissions

        override suspend fun getStepsForPeriod(startTime: Instant, endTime: Instant): DomainResult<Long> =
            error("Unexpected data read")

        override suspend fun getHeartRateForPeriod(
            startTime: Instant,
            endTime: Instant,
        ): DomainResult<List<HeartRateSample>> = error("Unexpected data read")

        override suspend fun getAggregatedHeartRateForPeriod(
            startTime: Instant,
            endTime: Instant,
        ): DomainResult<HeartRateAggregatedStats> = error("Unexpected data read")
    }
}
