package com.antyzero.tastytrade.core.repository.di

import com.antyzero.tastytrade.core.repository.DefaultTastyTradeRepository
import com.antyzero.tastytrade.core.repository.TastyTradeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTastyTradeRepository(
        impl: DefaultTastyTradeRepository
    ): TastyTradeRepository
}