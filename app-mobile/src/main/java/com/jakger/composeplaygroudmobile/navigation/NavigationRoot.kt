package com.jakger.composeplaygroudmobile.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.runtime.result.rememberResultEventBusNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.jakger.core_designsystem.ui.theme.ComposePlaygroundTheme
import com.jakger.core_arch.navi.NavigationEntries

@Composable
fun NavigationRoot(
    features: Set<NavigationEntries>
) {
    val backStack = rememberNavBackStack(Home)

    val provider = remember(features) {
        entryProvider<NavKey> {
            features.forEach {
                it.install(this, backStack)
            }
        }
    }

    ComposePlaygroundTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            NavDisplay(
                modifier = Modifier.padding(paddingValues),
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                entryDecorators = listOf(
                    rememberViewModelStoreNavEntryDecorator(),
                    rememberSaveableStateHolderNavEntryDecorator(), // replaced rememberSavedStateNavEntryDecorator
                    rememberResultEventBusNavEntryDecorator(),
                ),
                entryProvider = provider
//                entryProvider = entryProvider {
//                    entry<Home> {
//                        HomeScreen(
//                            backStack = backStack
//                        )
//                    }
//                    entry<Buttons> {
//                        ButtonsScreen()
//                    }
//                },
            )
        }
    }
}