package com.jakger.feature_ghcrange.usecase

import com.jakger.core_common.domain.DomainResult
import com.jakger.core_common.logging.AppLog
import com.jakger.feature_ghcrange.model.HealthConnectAccess
import com.jakger.feature_ghcrange.model.HealthConnectError
import com.jakger.feature_ghcrange.model.HeartRateAggregatedStats
import com.jakger.feature_ghcrange.repository.HealthRepository
import jakarta.inject.Inject
import java.time.ZonedDateTime
import kotlin.coroutines.cancellation.CancellationException

class GetHeartRateLastYearUseCase @Inject constructor(
    private val healthRepository: HealthRepository,
) {
    /** Rolling twelve calendar months, ending at the current instant in the phone's timezone. */
    suspend operator fun invoke(
        now: ZonedDateTime = ZonedDateTime.now(),
    ): DomainResult<HeartRateAggregatedStats> = try {
        when (healthRepository.checkHistoryAccess()) {
            HealthConnectAccess.Granted -> healthRepository.getAggregatedHeartRateForPeriod(
                startTime = now.minusMonths(12).toInstant(),
                endTime = now.toInstant(),
            )
            HealthConnectAccess.NeedsPermission -> DomainResult.Failure(HealthConnectError.PermissionDenied)
            HealthConnectAccess.Unsupported -> DomainResult.Failure(HealthConnectError.HistoryReadUnsupported)
            HealthConnectAccess.NeedsInstall,
            HealthConnectAccess.Unavailable -> DomainResult.Failure(HealthConnectError.ServiceUnavailable)
        }
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: Exception) {
        AppLog.e(throwable = exception) { "Unable to read heart rate for the last twelve months" }
        DomainResult.Failure(HealthConnectError.ServiceUnavailable)
    }
}
