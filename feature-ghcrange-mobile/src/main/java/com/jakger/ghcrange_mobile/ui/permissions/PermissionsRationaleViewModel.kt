package com.jakger.ghcrange_mobile.ui.permissions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.jakger.core_arch.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PermissionsRationaleViewModel @Inject constructor(
    reducer: PermissionsRationaleReducer,
    savedStateHandle: SavedStateHandle,
) : MviViewModel<PermissionsRationaleUiState, PermissionsRationaleUiEvent, PermissionsRationaleUiSideEffect>(reducer) {
    private val _uiState = MutableStateFlow(
        PermissionsRationaleUiState(
            isPermissionRequest = savedStateHandle[EXTRA_PERMISSION_EXPLANATION] ?: false,
        )
    )
    // Finishing is terminal and safe to replay if the Activity is recreated.
    private val _uiSideEffect = MutableSharedFlow<PermissionsRationaleUiSideEffect>(replay = 1)

    override val uiState = _uiState.asStateFlow()
    override val uiSideEffect = _uiSideEffect.asSharedFlow()

    override fun dispatchUiEvent(event: PermissionsRationaleUiEvent) {
        viewModelScope.launch {
            val (state, effect) = reducer(event, _uiState.value)
            _uiState.value = state
            if (effect != PermissionsRationaleUiSideEffect.None) _uiSideEffect.emit(effect)
        }
    }
}
