package com.jakger.ghcrange_mobile.ui.main

import androidx.annotation.MainThread
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.jakger.core_arch.mvi.MviViewModel
import com.jakger.core_common.domain.fold
import com.jakger.core_common.domain.DomainError
import com.jakger.core_common.logging.AppLog
import com.jakger.feature_ghcrange.model.HealthConnectAccess
import com.jakger.feature_ghcrange.model.HealthConnectError
import com.jakger.feature_ghcrange.usecase.CheckHealthConnectAvailabilityUseCase
import com.jakger.feature_ghcrange.usecase.GetHeartRateYesterdayUseCase
import com.jakger.feature_ghcrange.usecase.GetHeartRateLastYearUseCase
import com.jakger.feature_ghcrange.usecase.GetRestingHeartRateYesterdayUseCase
import com.jakger.feature_ghcrange.usecase.GetRestingHeartRateLastYearUseCase
import com.jakger.feature_ghcrange.usecase.GetStepsForYesterdayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class GhcRangeViewModel @Inject constructor(
    reducer: GhcRangeReducer,
    val getStepsForYesterdayUseCase: GetStepsForYesterdayUseCase,
    val getHeartRateYesterdayUseCase: GetHeartRateYesterdayUseCase,
    private val getHeartRateLastYearUseCase: GetHeartRateLastYearUseCase,
    private val getRestingHeartRateYesterdayUseCase: GetRestingHeartRateYesterdayUseCase,
    private val getRestingHeartRateLastYearUseCase: GetRestingHeartRateLastYearUseCase,
    val checkHealthConnectAvailabilityUseCase: CheckHealthConnectAvailabilityUseCase,
    private val savedStateHandle: SavedStateHandle,
) : MviViewModel<GhcRangeUiState, GhcRangeUiEvent, GhcRangeUiSideEffect>(reducer) {

    private val restoredPermissionStage = GhcRangePermissionStage.restore(
        savedStateHandle[GhcRangePermissionStage.SAVED_STATE_KEY]
    )
    private val _uiState: MutableStateFlow<GhcRangeUiState> =
        MutableStateFlow(restoredPermissionStage?.uiState ?: GhcRangeUiState.Initial)
    private val _uiSideEffect = MutableSharedFlow<GhcRangeUiSideEffect>(
        extraBufferCapacity = 1,
    )

    override val uiState: StateFlow<GhcRangeUiState> = _uiState
    override val uiSideEffect: SharedFlow<GhcRangeUiSideEffect> = _uiSideEffect

    private val _navigationEffects =
        Channel<GhcRangeUiSideEffect.Navigation>(Channel.BUFFERED)
    val navigationEffects: Flow<GhcRangeUiSideEffect.Navigation> =
        _navigationEffects.receiveAsFlow()

    private val _uiEffect =
        Channel<GhcRangeUiSideEffect.UiEffect>(Channel.BUFFERED)
    val uiEffects: Flow<GhcRangeUiSideEffect.UiEffect> =
        _uiEffect.receiveAsFlow()

    init {
        if (restoredPermissionStage == null) {
            dispatchUiEvent(GhcRangeUiEvent.CheckHealthDataAvailability)
        }
    }

    @MainThread
    override fun dispatchUiEvent(event: GhcRangeUiEvent) {
        AppLog.d { "Processing intent: $event" }
        val (state, effect) = reducer(event, _uiState.value)
        _uiState.value = state
        // Save synchronously so a launch acknowledgement is recorded before opening another Activity.
        savedStateHandle[GhcRangePermissionStage.SAVED_STATE_KEY] = GhcRangePermissionStage
            .from(state)?.name
        handleSideEffect(effect)
    }

    private fun handleSideEffect(effect: GhcRangeUiSideEffect) {
        if (effect == GhcRangeUiSideEffect.None) return

        viewModelScope.launch {
            _uiSideEffect.emit(effect)
            AppLog.d { "Handling effect: ${effect.javaClass.simpleName}" }
            when (effect) {
                is GhcRangeUiSideEffect.Navigation -> {
                    _navigationEffects.send(effect)
                }
                is GhcRangeUiSideEffect.UiEffect -> {
                    _uiEffect.send(effect)
                }
                GhcRangeUiSideEffect.FetchHealthData -> fetchHealthData()
                GhcRangeUiSideEffect.CheckHealthDataPermissions -> checkHealthConnectAvailability(checkOnly = true)
                GhcRangeUiSideEffect.CheckHealthDataAvailability -> checkHealthConnectAvailability()
                GhcRangeUiSideEffect.None -> Unit
            }
        }
    }

    private suspend fun checkHealthConnectAvailability(checkOnly: Boolean = false) {
        checkHealthConnectAvailabilityUseCase().run {
            AppLog.d { "HealthConnectAccess value: $this" }
            when (this) {
                is HealthConnectAccess.Granted -> {
                    if (!checkOnly) dispatchUiEvent(GhcRangeUiEvent.HealthPermissionsGranted)
                }

                is HealthConnectAccess.NeedsPermission -> {
                    dispatchUiEvent(
                        if (checkOnly) GhcRangeUiEvent.HealthPermissionsDenied
                        else GhcRangeUiEvent.HealthPermissionsRequired
                    )
                }

                is HealthConnectAccess.NeedsInstall -> {
                    dispatchUiEvent(GhcRangeUiEvent.HealthDataUnsupported)
                }

                HealthConnectAccess.Unavailable,
                HealthConnectAccess.Unsupported -> {
                    dispatchUiEvent(GhcRangeUiEvent.HealthDataUnsupported)
                }
            }
        }
    }

    private suspend fun fetchHealthData() {
        val yesterday = getHeartRateYesterdayUseCase().fold(
            onSuccess = { it },
            onFailure = {
                handleHealthDataFailure(it)
                return
            },
        )
        val lastYear = getHeartRateLastYearUseCase().fold(
            onSuccess = { it },
            onFailure = {
                handleHealthDataFailure(it)
                return
            },
        )
        val yesterdayResting = getRestingHeartRateYesterdayUseCase().fold(
            onSuccess = { it },
            onFailure = {
                handleHealthDataFailure(it)
                return
            },
        )
        val lastYearResting = getRestingHeartRateLastYearUseCase().fold(
            onSuccess = { it },
            onFailure = {
                handleHealthDataFailure(it)
                return
            },
        )
        dispatchUiEvent(
            GhcRangeUiEvent.HeartRateFetched(yesterday, lastYear, yesterdayResting, lastYearResting)
        )
    }

    private fun handleHealthDataFailure(error: DomainError) {
        dispatchUiEvent(
            if (error == HealthConnectError.PermissionDenied) GhcRangeUiEvent.HealthPermissionsDenied
            else GhcRangeUiEvent.HealthDataUnsupported
        )
    }
}
