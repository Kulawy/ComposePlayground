package com.jakger.feature_ghcrange.usecase

import com.jakger.core_common.domain.DomainResult
import com.jakger.feature_ghcrange.model.HealthParametersAggregatedStats
import com.jakger.feature_ghcrange.repository.HealthRepository
import jakarta.inject.Inject
import java.time.Instant

class GetHealthParametersForPeriodUseCase @Inject constructor(
    private val healthRepository: HealthRepository,
) {
    suspend operator fun invoke(
        startTime: Instant,
        endTime: Instant,
    ): DomainResult<HealthParametersAggregatedStats> {
        val activeCalories = when (val result = healthRepository.getActiveCaloriesBurnedForPeriod(startTime, endTime)) {
            is DomainResult.Success -> result.data
            is DomainResult.Failure -> null //error handling missing, in this form it lose information
        }
        val totalCalories = when (val result = healthRepository.getTotalCaloriesBurnedForPeriod(startTime, endTime)) {
            is DomainResult.Success -> result.data
            is DomainResult.Failure -> null //error handling missing
        }
        val steps = when (val result = healthRepository.getStepsForPeriod(startTime, endTime)) {
            is DomainResult.Success -> result.data
            is DomainResult.Failure -> null //error handling missing
        }
        val distance = when (val result = healthRepository.getDistanceForPeriod(startTime, endTime)) {
            is DomainResult.Success -> result.data
            is DomainResult.Failure -> null //error handling missing
        }
        val floors = when (val result = healthRepository.getFloorsClimbedForPeriod(startTime, endTime)) {
            is DomainResult.Success -> result.data
            is DomainResult.Failure -> null //error handling missing
        }

        return DomainResult.Success(
            HealthParametersAggregatedStats(
                floors = floors,
                distanceMeters = distance,
                steps = steps,
                activeCaloriesBurnedKcal = activeCalories,
                totalCaloriesBurnedKcal = totalCalories,
            )
        )
    }
}
