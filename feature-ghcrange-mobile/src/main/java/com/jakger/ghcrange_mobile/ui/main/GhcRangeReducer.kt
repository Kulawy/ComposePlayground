package com.jakger.ghcrange_mobile.ui.main

import com.jakger.core_arch.mvi.MviReducer
import com.jakger.feature_ghcrange.model.toUi
import jakarta.inject.Inject

class GhcRangeReducer @Inject constructor() :
    MviReducer<GhcRangeUiState, GhcRangeUiEvent, GhcRangeUiSideEffect> {
    override fun invoke(
        event: GhcRangeUiEvent,
        state: GhcRangeUiState
    ): Pair<GhcRangeUiState, GhcRangeUiSideEffect> {
        if (event == GhcRangeUiEvent.ScreenResumed) {
            return state to (GhcRangePermissionStage.from(state)?.pendingEffect ?: GhcRangeUiSideEffect.None)
        }
        if (event is GhcRangeUiEvent.PermissionUiEffectLaunched) {
            return acknowledgeLaunch(state, event.effect) to GhcRangeUiSideEffect.None
        }
        if (event == GhcRangeUiEvent.PermissionUiLaunchFailed && GhcRangePermissionStage.from(state) != null) {
            return GhcRangeUiState.Error.HealthDataNotAvailable to GhcRangeUiSideEffect.None
        }

        return when (state) {
            is GhcRangeUiState.Initial -> state.reduce(event)
            is GhcRangeUiState.AwaitingPermissionExplanation -> when (event) {
                GhcRangeUiEvent.RequestPermission ->
                    GhcRangeUiState.AwaitingPermissionResult() to GhcRangeUiSideEffect.UiEffect.RequestPermissions
                GhcRangeUiEvent.HealthPermissionsDenied ->
                    GhcRangeUiState.Error.PermissionsNotGranted to GhcRangeUiSideEffect.None
                GhcRangeUiEvent.OnBackPressed -> state to GhcRangeUiSideEffect.Navigation.Back
                else -> illegalEventForState(state, event)
            }
            is GhcRangeUiState.AwaitingPermissionResult -> when (event) {
                GhcRangeUiEvent.HealthPermissionsGranted ->
                    GhcRangeUiState.Loading to GhcRangeUiSideEffect.FetchHealthData
                GhcRangeUiEvent.HealthPermissionsDenied ->
                    GhcRangeUiState.Error.PermissionsNotGranted to GhcRangeUiSideEffect.None
                GhcRangeUiEvent.OnBackPressed -> state to GhcRangeUiSideEffect.Navigation.Back
                else -> illegalEventForState(state, event)
            }
            is GhcRangeUiState.AwaitingPermissionSettings -> when (event) {
                GhcRangeUiEvent.PermissionSettingsClosed ->
                    GhcRangeUiState.Initial to GhcRangeUiSideEffect.CheckHealthDataAvailability
                GhcRangeUiEvent.OnBackPressed -> state to GhcRangeUiSideEffect.Navigation.Back
                else -> illegalEventForState(state, event)
            }
            is GhcRangeUiState.Display -> state.reduce(event)
            is GhcRangeUiState.Loading -> state.reduce(event)
            is GhcRangeUiState.Error -> state.reduce(event)
        }
    }

    private fun acknowledgeLaunch(
        state: GhcRangeUiState,
        effect: GhcRangeUiSideEffect.UiEffect,
    ): GhcRangeUiState {
        if (GhcRangePermissionStage.from(state)?.pendingEffect != effect) return state

        return when (state) {
            is GhcRangeUiState.AwaitingPermissionExplanation -> state.copy(launchPending = false)
            is GhcRangeUiState.AwaitingPermissionResult -> state.copy(launchPending = false)
            is GhcRangeUiState.AwaitingPermissionSettings -> state.copy(launchPending = false)
            else -> state
        }
    }


    private fun GhcRangeUiState.Loading.reduce(
        event: GhcRangeUiEvent
    ): Pair<GhcRangeUiState, GhcRangeUiSideEffect> =
        when (event) {
            is GhcRangeUiEvent.HeartRateFetched ->
                GhcRangeUiState.Display(event.heartRate.toUi()) to GhcRangeUiSideEffect.None

            GhcRangeUiEvent.HealthDataUnsupported ->
                GhcRangeUiState.Error.HealthDataNotAvailable to GhcRangeUiSideEffect.None

            GhcRangeUiEvent.OnBackPressed -> this to GhcRangeUiSideEffect.Navigation.Back
            else -> illegalEventForState(this, event)
        }

    private fun GhcRangeUiState.Error.reduce(
        event: GhcRangeUiEvent
    ): Pair<GhcRangeUiState, GhcRangeUiSideEffect> =
        when (event) {
            GhcRangeUiEvent.OnBackPressed -> this to GhcRangeUiSideEffect.Navigation.Back
            GhcRangeUiEvent.CheckHealthDataAvailability ->
                GhcRangeUiState.Initial to GhcRangeUiSideEffect.CheckHealthDataAvailability
            GhcRangeUiEvent.ManagePermissions ->
                GhcRangeUiState.AwaitingPermissionSettings() to GhcRangeUiSideEffect.UiEffect.OpenPermissionSettings
            else -> illegalEventForState(this, event)
        }

    private fun GhcRangeUiState.Display.reduce(
        event: GhcRangeUiEvent
    ): Pair<GhcRangeUiState, GhcRangeUiSideEffect> =
        when (event) {
            GhcRangeUiEvent.OnBackPressed -> this to GhcRangeUiSideEffect.Navigation.Back
            else -> illegalEventForState(this, event)
        }

    private fun GhcRangeUiState.Initial.reduce(
        event: GhcRangeUiEvent
    ): Pair<GhcRangeUiState, GhcRangeUiSideEffect> =
        when (event) {
            is GhcRangeUiEvent.OnBackPressed -> this to GhcRangeUiSideEffect.Navigation.Back
            is GhcRangeUiEvent.CheckHealthDataAvailability ->
                this to GhcRangeUiSideEffect.CheckHealthDataAvailability

            is GhcRangeUiEvent.HeartRateFetched ->
                GhcRangeUiState.Display(
                    event.heartRate.toUi()
                ) to GhcRangeUiSideEffect.None

            is GhcRangeUiEvent.HealthDataUnsupported ->
                GhcRangeUiState.Error.HealthDataNotAvailable to GhcRangeUiSideEffect.None

            is GhcRangeUiEvent.HealthPermissionsGranted ->
                GhcRangeUiState.Loading to GhcRangeUiSideEffect.FetchHealthData

            is GhcRangeUiEvent.HealthPermissionsDenied ->
                GhcRangeUiState.Error.PermissionsNotGranted to GhcRangeUiSideEffect.None

            GhcRangeUiEvent.HealthPermissionsRequired ->
                GhcRangeUiState.AwaitingPermissionExplanation() to GhcRangeUiSideEffect.UiEffect.ExplainPermissions

            else -> {
                illegalEventForState(this, event)
            }
        }

    private fun illegalEventForState(
        state: GhcRangeUiState,
        event: GhcRangeUiEvent
    ): Pair<GhcRangeUiState, GhcRangeUiSideEffect> {
//        logInfo(
//            tag = "BankStatementViewModel",
//            message = { "reducer Illegal event for state: $state, $event" }
//        )
        return state to GhcRangeUiSideEffect.None
    }
}



