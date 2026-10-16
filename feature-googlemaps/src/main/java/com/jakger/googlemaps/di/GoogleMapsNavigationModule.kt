package com.jakger.googlemaps.di

import com.jakger.core_arch.navi.NavigationEntries
import com.jakger.googlemaps.navigation.GoogleMapsEntries
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class GoogleMapsNavigationModule {

    @Binds
    @IntoSet
    abstract fun bindGoogleMapsNavGraph(entries: GoogleMapsEntries) : NavigationEntries
}