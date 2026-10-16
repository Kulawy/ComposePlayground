package com.jakger.composeplaygroudmobile.navigation

import androidx.navigation3.runtime.NavKey

interface AppNavigator{
    fun push(key: NavKey)
    fun back()
}

//interface FeatureEntries {
//    fun
//}