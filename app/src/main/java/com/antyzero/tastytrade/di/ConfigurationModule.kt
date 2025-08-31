package com.antyzero.tastytrade.di

import com.antyzero.tastytrade.core.auth.AuthUriConfiguration
import com.antyzero.tastytrade.core.auth.AuthUriConfiguration.Companion.QUERY_CLIENT_ID
import com.antyzero.tastytrade.core.auth.AuthUriConfiguration.Companion.QUERY_REDIRECT_URI
import com.antyzero.tastytrade.core.auth.AuthUriConfiguration.Companion.QUERY_RESPONSE_TYPE
import com.antyzero.tastytrade.core.auth.AuthUriConfiguration.Companion.QUERY_SCOPE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Ideally those configurations should be in app module, one of benefits: we cun spin up another app
 * easily and just provide different configuration
 */

@Module
@InstallIn(SingletonComponent::class)
class ConfigurationModule {

    @Provides
    @Singleton
    fun provideAuthUriConfiguration(): AuthUriConfiguration {

        val Sandbox = AuthUriConfiguration(
            authority = "cert-my.staging-tasty.works",
            path = "auth.html",
            queryParams = listOf(
                QUERY_CLIENT_ID to "2912f9e1-91a7-4665-ab77-05ddf5d00414",
                QUERY_REDIRECT_URI to "com.antyzero.tastytrade://oauth2redirect",
                QUERY_RESPONSE_TYPE to "code",
                QUERY_SCOPE to "read trade"
            )
        )

        return Sandbox
    }
}