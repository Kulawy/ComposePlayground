package com.jakger.ghcrange_mobile.ui.permissions

import com.jakger.core_arch.mvi.MviReducer
import jakarta.inject.Inject

class PermissionsRationaleReducer @Inject constructor() :
    MviReducer<PermissionsRationaleUiState, PermissionsRationaleUiEvent, PermissionsRationaleUiSideEffect> {
    override fun invoke(
        event: PermissionsRationaleUiEvent,
        state: PermissionsRationaleUiState,
    ): Pair<PermissionsRationaleUiState, PermissionsRationaleUiSideEffect> {
        if (state.isClosing) return state to PermissionsRationaleUiSideEffect.None

        return when (event) {
            PermissionsRationaleUiEvent.Continue -> {
                // The external privacy-policy entry point must not start another request.
                if (state.isPermissionRequest) {
                    state.copy(isClosing = true) to PermissionsRationaleUiSideEffect.Finish(true)
                } else {
                    state to PermissionsRationaleUiSideEffect.None
                }
            }
            PermissionsRationaleUiEvent.NotNow,
            PermissionsRationaleUiEvent.Back ->
                state.copy(isClosing = true) to PermissionsRationaleUiSideEffect.Finish(false)
        }
    }
}
