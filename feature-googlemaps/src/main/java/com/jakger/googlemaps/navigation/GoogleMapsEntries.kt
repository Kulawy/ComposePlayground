package com.jakger.googlemaps.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.jakger.core_arch.navi.AppNavigator
import com.jakger.core_arch.navi.NavigationEntries
import com.jakger.feature_googlemaps_api.GoogleMapsNavFlow
import com.jakger.googlemaps.ui.main.GoogleMapsScreen
import javax.inject.Inject

class GoogleMapsEntries @Inject constructor() : NavigationEntries {
    override fun install(
        scope: EntryProviderScope<NavKey>,
        navigator: AppNavigator
    ) = with(scope) {
        entry<GoogleMapsNavFlow.Main> { key ->
            GoogleMapsScreen()
        }
    }
}