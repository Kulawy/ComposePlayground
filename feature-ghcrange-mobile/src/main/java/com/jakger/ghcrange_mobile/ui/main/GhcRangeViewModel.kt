package com.jakger.ghcrange_mobile.ui.main

import androidx.lifecycle.viewModelScope
import com.jakger.core_arch.mvi.MviViewModel
import com.jakger.core_common.domain.fold
import com.jakger.core_common.logging.AppLog
import com.jakger.feature_ghcrange.model.HealthConnectAccess
import com.jakger.feature_ghcrange.usecase.CheckHealthConnectAvailabilityUseCase
import com.jakger.feature_ghcrange.usecase.GetHeartRateYesterdayUseCase
import com.jakger.feature_ghcrange.usecase.GetStepsForYesterdayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import jakarta.inject.Inject
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class GhcRangeViewModel @Inject constructor(
    reducer: GhcRangeReducer,
    val getStepsForYesterdayUseCase: GetStepsForYesterdayUseCase,
    val getHeartRateYesterdayUseCase: GetHeartRateYesterdayUseCase,
    val checkHealthConnectAvailabilityUseCase: CheckHealthConnectAvailabilityUseCase,
) : MviViewModel<GhcRangeUiState, GhcRangeUiEvent, GhcRangeUiSideEffect>(reducer) {

    private val _uiState: MutableStateFlow<GhcRangeUiState> =
        MutableStateFlow(GhcRangeUiState.Initial)
    private val _uiSideEffect = MutableSharedFlow<GhcRangeUiSideEffect>(
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 1,
    )
    private val intents = Channel<GhcRangeUiEvent>()

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

    override fun dispatchUiEvent(event: GhcRangeUiEvent) {
        viewModelScope.launch {
            intents.send(event)
        }
    }

    init {
        consumeUiIntents()
        consumeUiSideEffects()

        dispatchUiEvent(GhcRangeUiEvent.CheckHealthDataAvailability)
    }

    private fun consumeUiIntents() {
        viewModelScope.launch {
            intents.consumeEach { intent ->
                AppLog.d { "Processing intent: $$intent" }
                val (state, effect) = reducer(event = intent, uiState.value)
                _uiState.value = state
                _uiSideEffect.emit(effect)
            }
        }
    }

    private fun consumeUiSideEffects() {
        viewModelScope.launch {
            _uiSideEffect.collect { effect ->
                AppLog.d { "Handling effect: ${effect.javaClass.simpleName}" }
                when (effect) {
                    is GhcRangeUiSideEffect.Navigation -> {
                        _navigationEffects.send(effect)
                    }
                    is GhcRangeUiSideEffect.UiEffect -> {
                        _uiEffect.send(effect)
                    }

                    is GhcRangeUiSideEffect.FetchHealthData ->
                        fetchHealthData()

                    is GhcRangeUiSideEffect.CheckHealthDataAvailability ->
                        checkHealthConnectAvailability()

                    is GhcRangeUiSideEffect.None -> { /* no-op */
                    }
                }
            }
        }
    }

    private suspend fun checkHealthConnectAvailability() {
        checkHealthConnectAvailabilityUseCase().run {
            AppLog.d { "HealthConnectAccess value: $this" }
            when (this) {
                is HealthConnectAccess.Granted -> {
                    fetchHealthData()
                }

                is HealthConnectAccess.NeedsPermission -> {
                    if (_uiState.value is GhcRangeUiState.Initial){
                        dispatchUiEvent(GhcRangeUiEvent.RequestPermission)
                    }
                    else {
                        dispatchUiEvent(GhcRangeUiEvent.HealthPermissionsDenied)
                    }
                }

                is HealthConnectAccess.NeedsInstall -> {
                    dispatchUiEvent(GhcRangeUiEvent.HealthDataUnsupported)
                }

                is HealthConnectAccess.Unsupported -> {
                    dispatchUiEvent(GhcRangeUiEvent.HealthDataUnsupported)
                }
            }
        }
    }

    private suspend fun fetchHealthData() {
        getHeartRateYesterdayUseCase().fold(
            onSuccess = {
                dispatchUiEvent(
                    GhcRangeUiEvent.HeartRateFetched(it)
                )
            },
            onFailure = {
                dispatchUiEvent(GhcRangeUiEvent.HealthDataUnsupported)
            }
        )
    }
}
