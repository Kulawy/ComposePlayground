package com.jakger.feature_maplibremaps.ui.main

import androidx.compose.runtime.Stable
import com.jakger.core_arch.mvi.UiState
import java.math.BigDecimal

sealed class MapLibreUiState : UiState {
    data object Loading : MapLibreUiState()
    data object Error : MapLibreUiState()

    @Stable
    data class View(
        val zoom: Double = 15.0,
        val points: List<MapPoint> = listOf(),
        val selectedPoint: MapPoint? = null,
        val menuExpanded: Boolean = false
    ) : MapLibreUiState()
}

data class MapPoint(
    val lat: BigDecimal,
    val lon: BigDecimal,
    val name: String
)
