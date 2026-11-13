package com.jakger.feature_ghcrange.usecase

import com.jakger.core_common.domain.DomainResult
import com.jakger.feature_ghcrange.repository.HealthRepository
import jakarta.inject.Inject

class GetStepsForYesterdayUseCase @Inject constructor(
    private val healthRepository: HealthRepository
) {
    suspend operator fun invoke(): DomainResult<Long?> {
        val startTime = java.time.LocalDate
            .now()
            .atStartOfDay(java.time.ZoneId.systemDefault())
            .minusDays(1L)
            .toInstant()
        val endTime = java.time.LocalDate
            .now()
            .atStartOfDay(java.time.ZoneId.systemDefault())
            .toInstant()

        return healthRepository.getStepsForPeriod(startTime, endTime)
    }
}
