package com.jakger.core_arch.mvi

/***
 *
 * each state should implement extended function
 * reduce(event: UiIntent): Pair<UiState, UiSideEffect>
 * reduce function updates actual state by event and returns new state and side effect
 */
interface MviReducer<S: UiState, I: UiEvent, E: UiSideEffect>{
    operator fun invoke(event: I, state: S): Pair<S, E>
}
