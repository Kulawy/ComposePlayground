package com.jakger.ghcrange_mobile.ui.main

internal enum class GhcRangePermissionStage(
    val uiState: GhcRangeUiState,
    val pendingEffect: GhcRangeUiSideEffect.UiEffect? = null,
) {
    EXPLANATION_PENDING(
        GhcRangeUiState.AwaitingPermissionExplanation(),
        GhcRangeUiSideEffect.UiEffect.ExplainPermissions,
    ),
    EXPLANATION_LAUNCHED(GhcRangeUiState.AwaitingPermissionExplanation(launchPending = false)),
    PERMISSIONS_PENDING(
        GhcRangeUiState.AwaitingPermissionResult(),
        GhcRangeUiSideEffect.UiEffect.RequestPermissions,
    ),
    PERMISSIONS_LAUNCHED(GhcRangeUiState.AwaitingPermissionResult(launchPending = false)),
    SETTINGS_PENDING(
        GhcRangeUiState.AwaitingPermissionSettings(),
        GhcRangeUiSideEffect.UiEffect.OpenPermissionSettings,
    ),
    SETTINGS_LAUNCHED(GhcRangeUiState.AwaitingPermissionSettings(launchPending = false));

    companion object {
        const val SAVED_STATE_KEY = "health_permission_stage"

        fun restore(value: String?): GhcRangePermissionStage? = entries.firstOrNull { it.name == value }

        fun from(state: GhcRangeUiState): GhcRangePermissionStage? =
            entries.firstOrNull { it.uiState == state }
    }
}
