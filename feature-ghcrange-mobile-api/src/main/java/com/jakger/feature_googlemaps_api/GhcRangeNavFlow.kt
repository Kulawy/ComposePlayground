package com.jakger.feature_googlemaps_api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class GhcRangeNavFlow : NavKey {

    @Serializable
    data object Main : GhcRangeNavFlow()
}