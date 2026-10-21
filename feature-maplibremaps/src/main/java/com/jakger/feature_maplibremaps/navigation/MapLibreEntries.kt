package com.jakger.feature_maplibremaps.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.jakger.core_arch.navi.AppNavigator
import com.jakger.core_arch.navi.NavigationEntries
import com.jakger.feature_maplibremaps.ui.main.MapLibreScreen
import com.jakger.feature_maplibremaps_api.MapLibreMapsNavigationFlow
import javax.inject.Inject

class MapLibreEntries @Inject constructor() : NavigationEntries {
    override fun install(
        scope: EntryProviderScope<NavKey>,
        navigator: AppNavigator
    ) = with(scope) {
        entry<MapLibreMapsNavigationFlow.Main> { key ->
            MapLibreScreen()
        }
    }
}