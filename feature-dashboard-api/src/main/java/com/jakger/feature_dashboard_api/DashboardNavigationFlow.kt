package com.jakger.feature_dashboard_api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class DashboardNavigationFlow : NavKey {
    @Serializable
    data object Home : DashboardNavigationFlow()

    @Serializable
    data object Buttons : DashboardNavigationFlow()
}