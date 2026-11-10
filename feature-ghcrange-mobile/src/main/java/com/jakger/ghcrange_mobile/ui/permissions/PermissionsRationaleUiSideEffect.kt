package com.jakger.ghcrange_mobile.ui.permissions

import com.jakger.core_arch.mvi.UiSideEffect

sealed interface PermissionsRationaleUiSideEffect : UiSideEffect {
    data object None : PermissionsRationaleUiSideEffect
    data class Finish(val continueToPermissions: Boolean) : PermissionsRationaleUiSideEffect
}
