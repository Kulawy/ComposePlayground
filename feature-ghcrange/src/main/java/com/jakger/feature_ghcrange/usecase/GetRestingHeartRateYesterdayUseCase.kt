package com.jakger.feature_ghcrange.usecase

import com.jakger.core_common.domain.DomainResult
import com.jakger.feature_ghcrange.model.HeartRateAggregatedStats
import com.jakger.feature_ghcrange.repository.HealthRepository
import jakarta.inject.Inject
import java.time.ZonedDateTime

class GetRestingHeartRateYesterdayUseCase @Inject constructor(
    private val healthRepository: HealthRepository,
) {
    suspend operator fun invoke(
        now: ZonedDateTime = ZonedDateTime.now(),
    ): DomainResult<HeartRateAggregatedStats> {
        val today = now.toLocalDate()
        val startTime = today.minusDays(1).atStartOfDay(now.zone).toInstant()
        val endTime = today.atStartOfDay(now.zone).toInstant()

        return healthRepository.getAggregatedRestingHeartRateForPeriod(startTime, endTime)
    }
}
