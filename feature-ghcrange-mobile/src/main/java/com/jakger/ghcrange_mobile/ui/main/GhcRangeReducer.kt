package com.jakger.ghcrange_mobile.ui.main

import com.jakger.core_arch.mvi.MviReducer
import jakarta.inject.Inject

class GhcRangeReducer @Inject constructor() :
    MviReducer<GhcRangeUiState, GhcRangeUiEvent, GhcRangeUiSideEffect> {
    override fun invoke(
        event: GhcRangeUiEvent,
        state: GhcRangeUiState
    ): Pair<GhcRangeUiState, GhcRangeUiSideEffect> =
        when (state) {
            is GhcRangeUiState.Initial -> state.reduce(event)
            is GhcRangeUiState.Display -> state.reduce(event)
            is GhcRangeUiState.Loading -> state.reduce(event)
            is GhcRangeUiState.Error -> state.reduce(event)
        }


    private fun GhcRangeUiState.Loading.reduce(
        event: GhcRangeUiEvent
    ): Pair<GhcRangeUiState, GhcRangeUiSideEffect> =
        when (event) {
            is GhcRangeUiEvent.HeartRateFetched ->
                GhcRangeUiState.Display(event.heartRate) to GhcRangeUiSideEffect.None

            GhcRangeUiEvent.OnBackPressed -> this to GhcRangeUiSideEffect.Navigation.Back
            else -> illegalEventForState(this, event)
        }

    private fun GhcRangeUiState.Error.reduce(
        event: GhcRangeUiEvent
    ): Pair<GhcRangeUiState, GhcRangeUiSideEffect> =
        when (event) {
            GhcRangeUiEvent.OnBackPressed -> this to GhcRangeUiSideEffect.Navigation.Back
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
                    event.heartRate
                ) to GhcRangeUiSideEffect.FetchHealthData

            is GhcRangeUiEvent.HealthDataUnsupported ->
                GhcRangeUiState.Error.HealthDataNotAvailable to GhcRangeUiSideEffect.None

            is GhcRangeUiEvent.HealthPermissionsGranted ->
                this to GhcRangeUiSideEffect.None

            is GhcRangeUiEvent.HealthPermissionsDenied ->
                GhcRangeUiState.Error.PermissionsNotGranted to GhcRangeUiSideEffect.None
//            is UiEvent.InitialDataLoaded -> {
//                event.toUiState(
//                    customStartDate = operationPeriodConstants.bankStatementDefaultCustomStartDate,
//                    customEndDate = operationPeriodConstants.today
//                ) to UiSideEffect.None
//            }
//
//            is UiEvent.GetInitialDataError -> {
//                analytics.screenDisplayed(
//                    ScreenFirebase.BANK_STATEMENT_ERROR_GENERAL,
//                    UiState.Error.InitializeError::class.java
//                )
//                UiState.Error.InitializeError(
//                    isFileComplaintVisible = coreModuleConfig
//                        .bankLocale.countryCode == Locale.PL.name
//                ) to UiSideEffect.None
//            }

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



