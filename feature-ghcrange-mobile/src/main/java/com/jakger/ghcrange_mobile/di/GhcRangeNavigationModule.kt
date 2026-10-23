package com.jakger.ghcrange_mobile.di

import com.jakger.core_arch.navi.NavigationEntries
import com.jakger.ghcrange_mobile.navigation.GhcRangeEntries
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class GhcRangeNavigationModule {

    @Binds
    @IntoSet
    abstract fun bindGhcRangeNavGraph(entries: GhcRangeEntries) : NavigationEntries
}