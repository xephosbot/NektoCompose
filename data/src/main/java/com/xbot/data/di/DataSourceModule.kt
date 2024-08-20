package com.xbot.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.xbot.data.source.TokenDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    @Singleton
    fun provideTokenDataSource(
        dataStore: DataStore<Preferences>
    ): TokenDataSource {
        return TokenDataSource(dataStore)
    }
}