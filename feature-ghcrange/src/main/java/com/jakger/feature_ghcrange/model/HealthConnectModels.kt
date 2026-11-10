package com.jakger.feature_ghcrange.model

import com.jakger.core_common.domain.DomainError

enum class HealthAvailability { AVAILABLE, NEEDS_INSTALL_OR_UPDATE, UNSUPPORTED }

sealed interface HealthConnectAccess {
    data object Granted : HealthConnectAccess
    data object NeedsPermission : HealthConnectAccess
    data object NeedsInstall : HealthConnectAccess
    data object Unsupported : HealthConnectAccess
    data object Unavailable : HealthConnectAccess
}

sealed interface HealthConnectError: DomainError {
    data object PermissionDenied : HealthConnectError
    data object ServiceUnavailable : HealthConnectError
}
