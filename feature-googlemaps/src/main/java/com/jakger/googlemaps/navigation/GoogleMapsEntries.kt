package com.jakger.googlemaps.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.jakger.core_arch.navi.NavigationEntries
import com.jakger.feature_googlemaps_api.GoogleMapsNavFlow
import javax.inject.Inject

class GoogleMapsEntries @Inject constructor() : NavigationEntries {
    override fun install(
        scope: EntryProviderScope<NavKey>,
        backStack: NavBackStack<NavKey>
//        navigator: AppNavigator,
    ) = with(scope) {
        entry<GoogleMapsNavFlow> { key ->
            // Screen implementation
        }
    }
}