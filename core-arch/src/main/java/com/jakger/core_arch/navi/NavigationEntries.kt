package com.jakger.core_arch.navi

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

interface NavigationEntries {
    fun install(
        scope: EntryProviderScope<NavKey>,
        navigator: AppNavigator
    )
}