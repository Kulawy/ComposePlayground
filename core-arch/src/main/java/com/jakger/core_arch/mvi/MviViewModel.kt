package com.jakger.core_arch.mvi

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

/***
 *  MviViewModel in my variation (lightweight) of MVI architecture, but I think better way is to use Timber State Machine
 *  UiState -> ui state representation
 *  UiEvent -> ui events representation,
 *  UiSideEffect -> Effects of events that are not changes state of the actual screen, like navigation (call to navigation service) to other screen or API call
 *  dispatchUiEvent function is, and should be, one and only connection from UI to ViewModel
 *  uiState is actual state of screen, and informs UI how it should render
 *  uiState updates should be only from reduce function in answer to UiEvent
 *  if UiEvent starts async action (communication with backend etc.) it should return UiSideEffect
 */

abstract class MviViewModel(
    open val reducer: MviReducer<UiState, UiEvent, UiSideEffect>,
): ViewModel() {
    abstract val uiState: StateFlow<UiState>
    abstract val uiSideEffect: SharedFlow<UiSideEffect>

    abstract fun dispatchUiEvent(event: UiEvent)
}