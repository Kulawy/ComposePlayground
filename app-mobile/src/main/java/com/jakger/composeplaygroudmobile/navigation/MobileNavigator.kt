package com.jakger.composeplaygroudmobile.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.jakger.core_arch.navi.AppNavigator

class MobileNavigator(
    private val backStack: NavBackStack<NavKey>
) : AppNavigator {
    override fun goTo(destination: NavKey) {
        backStack.add(destination)
    }

    override fun goBack() {
        if (backStack.size > 1) {
            backStack.removeLastOrNull()
        }
    }
}