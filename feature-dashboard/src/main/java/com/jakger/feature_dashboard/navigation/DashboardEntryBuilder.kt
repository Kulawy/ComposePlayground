package com.jakger.feature_dashboard.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.jakger.core_arch.navi.AppNavigator
import com.jakger.core_arch.navi.NavigationEntries
import com.jakger.feature_dashboard.buttons.ButtonsScreen
import com.jakger.feature_dashboard.home.HomeScreen
import com.jakger.feature_dashboard_api.DashboardNavigationFlow
import jakarta.inject.Inject

//fun EntryProviderScope<NavKey>.dashboardEntryBuilderEntryBuilder() {
//    entry<DashboardNavigationFlow.Home> {
//        HomeScreen(
//            backStack = backStack
//        )
//    }
//    entry<DashboardNavigationFlow.Buttons> {
//        ButtonsScreen()
//    }
//}

class DashboardEntries @Inject constructor() : NavigationEntries {
    override fun install(
        scope: EntryProviderScope<NavKey>,
        navigator: AppNavigator,
    ) = with(scope) {
        entry<DashboardNavigationFlow.Home> { key ->
            HomeScreen(
                navigator
            )
        }
        entry<DashboardNavigationFlow.Buttons> { key ->
            ButtonsScreen()
        }
    }
}