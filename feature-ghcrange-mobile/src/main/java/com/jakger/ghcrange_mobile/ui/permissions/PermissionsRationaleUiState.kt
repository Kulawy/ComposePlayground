package com.jakger.ghcrange_mobile.ui.permissions

import com.jakger.core_arch.mvi.UiState

data class PermissionsRationaleUiState(
    val isPermissionRequest: Boolean = false,
    val isClosing: Boolean = false,
) : UiState
