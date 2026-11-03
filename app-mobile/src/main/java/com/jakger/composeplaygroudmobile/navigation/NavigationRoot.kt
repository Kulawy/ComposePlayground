package com.jakger.composeplaygroudmobile.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.runtime.result.rememberResultEventBusNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.jakger.core_arch.navi.NavigationEntries
import com.jakger.core_designsystem.ui.theme.ComposePlaygroundTheme
import com.jakger.feature_dashboard_api.DashboardNavigationFlow

@Composable
fun NavigationRoot(
    features: Set<NavigationEntries>
) {
    val backStack = rememberNavBackStack(DashboardNavigationFlow.Home)
    val navigator = remember(backStack) {
        MobileNavigator(backStack)
    }
    val provider = remember(features) {
        entryProvider{
            features.forEach {
                it.install(this, navigator)
            }
        }
    }

    ComposePlaygroundTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
        ) { paddingValues ->
            NavDisplay(
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                entryDecorators = listOf(
                    rememberViewModelStoreNavEntryDecorator(),
                    rememberSaveableStateHolderNavEntryDecorator(), // replaced rememberSavedStateNavEntryDecorator
                    rememberResultEventBusNavEntryDecorator(),
                ),
                entryProvider = provider
            )
            Box(
                modifier = Modifier
                    .height(
//                        WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
                        paddingValues.calculateTopPadding()
                    )
                    .fillMaxWidth()
                    .background(Color(255f, 255f, 255f, 0.4f))
            )
        }
    }
}