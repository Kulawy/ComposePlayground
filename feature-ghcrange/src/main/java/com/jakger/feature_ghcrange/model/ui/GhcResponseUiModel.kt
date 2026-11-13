package com.jakger.feature_ghcrange.model.ui

// Model for eventually future error handling
sealed interface GhcResponseUiModel {

    data class Value<T>(
        val model: T?
    ) : GhcResponseUiModel

    data object MissingValue : GhcResponseUiModel
    data object ErrorOnDataFetch : GhcResponseUiModel
}