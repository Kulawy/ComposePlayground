package com.jakger.core_health.di

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import com.jakger.core_arch.di.IoDispatcher
import com.jakger.core_health.datasource.HealthConnectAggregatedDataSource
import com.jakger.core_health.datasource.HealthConnectDataSource
import com.jakger.core_health.repository.HealthConnectSdkRepositoryImpl
import com.jakger.feature_ghcrange.repository.HealthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
abstract class HealthModule {

    @Binds
    @Singleton
    abstract fun bindHealthRepository(
        impl: HealthConnectSdkRepositoryImpl
    ): HealthRepository
}

@Module
@InstallIn(SingletonComponent::class)
object HealthProvideModule {

    @Provides
    @Singleton
    fun provideHealthConnectClient(
        @ApplicationContext context: Context
    ): HealthConnectClient {
        return HealthConnectClient.getOrCreate(context)
    }

    @Provides
    @Singleton
    fun provideHealthConnectDataSource(
        @ApplicationContext context: Context,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): HealthConnectDataSource {
        return HealthConnectDataSource(
            context = context,
            ioDispatcher = ioDispatcher
        )
    }

    @Provides
    @Singleton
    fun provideHealthConnectAggregatedDataSource(
        @ApplicationContext context: Context,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): HealthConnectAggregatedDataSource =
        HealthConnectAggregatedDataSource(
            context = context,
            ioDispatcher = ioDispatcher
        )
}