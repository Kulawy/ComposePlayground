package com.jakger.ghcrange_mobile.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.jakger.core_arch.navi.AppNavigator
import com.jakger.core_arch.navi.NavigationEntries
import com.jakger.feature_googlemaps_api.GhcRangeNavFlow
import com.jakger.ghcrange_mobile.ui.main.GhcRangeScreen
import javax.inject.Inject

class GhcRangeEntries @Inject constructor() : NavigationEntries {
    override fun install(
        scope: EntryProviderScope<NavKey>,
        navigator: AppNavigator
    ) = with(scope) {
        entry<GhcRangeNavFlow.Main> { key ->
            GhcRangeScreen()
        }
    }
}