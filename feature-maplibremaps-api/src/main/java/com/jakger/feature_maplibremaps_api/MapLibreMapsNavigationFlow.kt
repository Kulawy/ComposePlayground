package com.jakger.feature_maplibremaps_api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class MapLibreMapsNavigationFlow : NavKey {

    @Serializable
    data object Main : MapLibreMapsNavigationFlow()
}