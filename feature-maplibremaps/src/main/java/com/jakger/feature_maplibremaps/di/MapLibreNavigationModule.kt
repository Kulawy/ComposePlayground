package com.jakger.feature_maplibremaps.di

import com.jakger.core_arch.navi.NavigationEntries
import com.jakger.feature_maplibremaps.navigation.MapLibreEntries
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class MapLibreNavigationModule {

    @Binds
    @IntoSet
    abstract fun bindGoogleMapsNavGraph(entries: MapLibreEntries) : NavigationEntries
}