package com.jakger.feature_dashboard.di

import com.jakger.core_arch.navi.NavigationEntries
import com.jakger.feature_dashboard.navigation.DashboardEntries
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class DashboardNavigationModule {

    @Binds
    @IntoSet
    abstract fun bindGoogleMapsNavGraph(entries: DashboardEntries) : NavigationEntries
}