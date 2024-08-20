package com.xbot.data.di

import android.content.Context
import com.xbot.data.repository.AuthRepositoryImpl
import com.xbot.data.repository.DeviceInfoProvider
import com.xbot.data.source.TokenDataSource
import com.xbot.domain.repository.AuthRepository
import com.xbot.socket.Client
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideConnectionRepository(
        client: Client,
        tokenDataSource: TokenDataSource,
        provider: DeviceInfoProvider
    ): AuthRepository {
        return AuthRepositoryImpl(client, tokenDataSource, provider)
    }

    @Provides
    @Singleton
    fun provideDeviceInfoProvider(@ApplicationContext context: Context): DeviceInfoProvider {
        return DeviceInfoProvider(context)
    }
}