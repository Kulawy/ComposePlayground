package com.jakger.core_health.di

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import com.jakger.core_health.repository.HealthConnectSdkRepositoryImpl
import com.jakger.feature_ghcrange.repository.HealthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

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
}