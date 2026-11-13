package com.jakger.feature_ghcrange.usecase

import com.jakger.core_common.domain.DomainResult
import com.jakger.feature_ghcrange.model.HealthAvailability
import com.jakger.feature_ghcrange.model.HealthConnectAccess
import com.jakger.feature_ghcrange.model.HealthConnectError
import com.jakger.feature_ghcrange.model.HeartRateAggregatedStats
import com.jakger.feature_ghcrange.model.HeartRateSample
import com.jakger.feature_ghcrange.repository.HealthRepository
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.coroutines.cancellation.CancellationException

class GetHeartRateLastYearUseCaseTest {
    @Test
    fun `reads rolling twelve calendar months including leap day using the phone timezone`() = runBlocking {
        val repository = Repository(HealthConnectAccess.Granted)
        val now = ZonedDateTime.of(2024, 2, 29, 14, 30, 0, 0, ZoneId.of("Europe/Warsaw"))

        assertEquals(DomainResult.Success(repository.stats), GetHeartRateLastYearUseCase(repository)(now))
        assertEquals(Instant.parse("2023-02-28T13:30:00Z"), repository.requestedStart)
        assertEquals(Instant.parse("2024-02-29T13:30:00Z"), repository.requestedEnd)
    }

    @Test
    fun `does not present a partial year when history permission is missing`() = runBlocking {
        val repository = Repository(HealthConnectAccess.NeedsPermission)
        assertEquals(
            DomainResult.Failure(HealthConnectError.PermissionDenied),
            GetHeartRateLastYearUseCase(repository)(),
        )
        assertEquals(null, repository.requestedStart)
    }

    @Test
    fun `unsupported history feature does not attempt an aggregate read`() = runBlocking {
        val repository = Repository(HealthConnectAccess.Unsupported)
        assertEquals(
            DomainResult.Failure(HealthConnectError.HistoryReadUnsupported),
            GetHeartRateLastYearUseCase(repository)(),
        )
        assertEquals(null, repository.requestedStart)
    }

    @Test
    fun `history access failures return a recoverable service error`() = runBlocking {
        val repository = Repository(HealthConnectAccess.Granted, IllegalStateException("Service unavailable"))
        assertEquals(
            DomainResult.Failure(HealthConnectError.ServiceUnavailable),
            GetHeartRateLastYearUseCase(repository)(),
        )
    }

    @Test
    fun `cancellation during history check is propagated`() = runBlocking {
        val cancellation = CancellationException("Screen closed")
        try {
            GetHeartRateLastYearUseCase(Repository(HealthConnectAccess.Granted, cancellation))()
            throw AssertionError("Cancellation should propagate")
        } catch (exception: CancellationException) {
            assertTrue(exception === cancellation)
        }
    }

    private class Repository(
        private val historyAccess: HealthConnectAccess,
        private val accessFailure: Exception? = null,
    ) : HealthRepository {
        val stats = HeartRateAggregatedStats(50, 70, 130, 1200)
        var requestedStart: Instant? = null
        var requestedEnd: Instant? = null

        override fun availability() = HealthAvailability.AVAILABLE
        override suspend fun checkPermissions() = true
        override suspend fun checkHistoryAccess(): HealthConnectAccess {
            accessFailure?.let { throw it }
            return historyAccess
        }

        override suspend fun getAggregatedHeartRateForPeriod(
            startTime: Instant,
            endTime: Instant,
        ): DomainResult<HeartRateAggregatedStats> {
            requestedStart = startTime
            requestedEnd = endTime
            return DomainResult.Success(stats)
        }

        override suspend fun getStepsForPeriod(startTime: Instant, endTime: Instant): DomainResult<Long> =
            error("Unexpected steps read")

        override suspend fun getActiveCaloriesBurnedForPeriod(
            startTime: Instant,
            endTime: Instant,
        ): DomainResult<Double> = error("Unexpected active calories read")

        override suspend fun getTotalCaloriesBurnedForPeriod(
            startTime: Instant,
            endTime: Instant,
        ): DomainResult<Double> = error("Unexpected total calories read")

        override suspend fun getDistanceForPeriod(
            startTime: Instant,
            endTime: Instant,
        ): DomainResult<Double> = error("Unexpected distance read")

        override suspend fun getFloorsClimbedForPeriod(
            startTime: Instant,
            endTime: Instant,
        ): DomainResult<Double> = error("Unexpected floors read")

        override suspend fun getHeartRateForPeriod(
            startTime: Instant,
            endTime: Instant,
        ): DomainResult<List<HeartRateSample>> = error("Unexpected raw heart-rate read")

        override suspend fun getAggregatedRestingHeartRateForPeriod(
            startTime: Instant,
            endTime: Instant,
        ): DomainResult<HeartRateAggregatedStats> = error("Unexpected resting heart-rate read")
    }
}
