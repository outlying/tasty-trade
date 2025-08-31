package com.antyzero.tastytrade.core.auth.di

import android.content.Context
import com.antyzero.tastytrade.core.auth.AuthConfiguration
import com.antyzero.tastytrade.core.auth.AuthUriConfiguration
import com.antyzero.tastytrade.core.auth.AuthUriProvider
import com.antyzero.tastytrade.core.auth.Authentication
import com.antyzero.tastytrade.core.auth.SharedPrefsTokenStorage
import com.antyzero.tastytrade.core.auth.StandardAuthentication
import com.antyzero.tastytrade.core.auth.TokenStorage
import com.antyzero.tastytrade.core.network.TastyTradeAuthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AuthModule {

    @Provides
    @Singleton
    fun provideAuthUriProvider(authUriConfiguration: AuthUriConfiguration): AuthUriProvider {
        return AuthUriProvider.Factory(authUriConfiguration)
    }

    @Provides
    @Singleton
    fun provideAuthConfiguration(): AuthConfiguration = AuthConfiguration(
        clientId = "2912f9e1-91a7-4665-ab77-05ddf5d00414",
        clientSecret = "cb63c6b8ccef7a004318a851ca6cf07aea5b4235", // Less than perfect
        redirectUri = "com.antyzero.tastytrade://oauth2redirect"
    )

    @Provides
    @Singleton
    fun provideTokenStorage(@ApplicationContext context: Context): TokenStorage {
        return SharedPrefsTokenStorage(context)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        authConfiguration: AuthConfiguration,
        tastyTradeAuthApi: TastyTradeAuthApi,
        tokenStorage: TokenStorage
    ): Authentication {
        return StandardAuthentication(
            authConfiguration,
            tokenStorage,
            tastyTradeAuthApi
        )
    }


}