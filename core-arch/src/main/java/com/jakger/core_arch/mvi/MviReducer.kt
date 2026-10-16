package com.jakger.core_arch.mvi

/***
 *
 * each state should implement extended function
 * reduce(event: UiEvent): Pair<UiState, UiSideEffect>
 * reduce function updates actual state by event and returns new state and side effect
 */
interface MviReducer<UiState, UiEvent, UiSideEffect> {
    operator fun invoke(event: UiEvent, state: UiState): Pair<UiState, UiSideEffect>
}
