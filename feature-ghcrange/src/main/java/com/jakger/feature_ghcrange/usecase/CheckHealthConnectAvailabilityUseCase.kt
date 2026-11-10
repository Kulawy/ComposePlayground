package com.jakger.feature_ghcrange.usecase

import com.jakger.core_common.logging.AppLog
import com.jakger.feature_ghcrange.model.HealthAvailability
import com.jakger.feature_ghcrange.model.HealthConnectAccess
import com.jakger.feature_ghcrange.repository.HealthRepository
import jakarta.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class CheckHealthConnectAvailabilityUseCase @Inject constructor(
    private val healthRepository: HealthRepository
) {
    suspend operator fun invoke(): HealthConnectAccess {
        AppLog.d { "Checking Health Connect access" }

        return try {
            when (healthRepository.availability()) {
                HealthAvailability.UNSUPPORTED -> HealthConnectAccess.Unsupported
                HealthAvailability.NEEDS_INSTALL_OR_UPDATE -> HealthConnectAccess.NeedsInstall
                HealthAvailability.AVAILABLE -> {
                    if (healthRepository.checkPermissions()) HealthConnectAccess.Granted
                    else HealthConnectAccess.NeedsPermission
                }
            }
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            AppLog.e(throwable = exception) { "Unable to check Health Connect access" }
            HealthConnectAccess.Unavailable
        }
    }
}
