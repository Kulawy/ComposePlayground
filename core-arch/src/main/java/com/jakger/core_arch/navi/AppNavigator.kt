package com.jakger.core_arch.navi

import androidx.navigation3.runtime.NavKey

interface AppNavigator {
    fun goTo(destination: NavKey)
    fun goBack()
}