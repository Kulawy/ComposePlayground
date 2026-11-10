package com.jakger.ghcrange_mobile.ui.permissions

import com.jakger.core_arch.mvi.UiEvent

sealed interface PermissionsRationaleUiEvent : UiEvent {
    data object Continue : PermissionsRationaleUiEvent
    data object NotNow : PermissionsRationaleUiEvent
    data object Back : PermissionsRationaleUiEvent
}
