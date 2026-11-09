package com.jakger.feature_ghcrange.usecase

import com.jakger.core_common.domain.DomainResult
import com.jakger.feature_ghcrange.model.HealthAvailability
import com.jakger.feature_ghcrange.model.HealthConnectAccess
import com.jakger.feature_ghcrange.repository.HealthRepository
import jakarta.inject.Inject

class CheckHealthConnectAvailabilityUseCase @Inject constructor(
    private val healthRepository: HealthRepository
) {
    suspend operator fun invoke(): HealthConnectAccess {

        val permissions = healthRepository.checkPermissions()
        val availability = healthRepository.availability()

        return when {
            availability == HealthAvailability.UNSUPPORTED -> HealthConnectAccess.Unsupported
            availability == HealthAvailability.NEEDS_INSTALL_OR_UPDATE -> HealthConnectAccess.NeedsInstall
            availability == HealthAvailability.AVAILABLE && permissions -> HealthConnectAccess.Granted // permissions + availability == HealthAvailability.AVAILABLE
            !permissions -> HealthConnectAccess.NeedsPermission
            else -> {
                
                HealthConnectAccess.Unsupported
            }
        }
    }
}