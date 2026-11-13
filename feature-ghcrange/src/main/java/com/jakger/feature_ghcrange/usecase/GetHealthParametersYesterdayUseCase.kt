package com.jakger.feature_ghcrange.usecase

import com.jakger.core_common.domain.DomainResult
import com.jakger.feature_ghcrange.model.HealthParametersAggregatedStats
import jakarta.inject.Inject
import java.time.ZonedDateTime

class GetHealthParametersYesterdayUseCase @Inject constructor(
    private val getHealthParametersForPeriodUseCase: GetHealthParametersForPeriodUseCase,
) {
    suspend operator fun invoke(
        now: ZonedDateTime = ZonedDateTime.now(),
    ): DomainResult<HealthParametersAggregatedStats> {
        val today = now.toLocalDate()
        val startTime = today.minusDays(1).atStartOfDay(now.zone).toInstant()
        val endTime = today.atStartOfDay(now.zone).toInstant()

        return getHealthParametersForPeriodUseCase(startTime, endTime)
    }
}
