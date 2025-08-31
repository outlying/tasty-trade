package com.antyzero.tastytrade.core.network.di

import com.antyzero.tastytrade.core.auth.Authentication
import com.antyzero.tastytrade.core.auth.TokenStorage
import com.antyzero.tastytrade.core.network.SANDBOX_BASE_URL
import com.antyzero.tastytrade.core.network.TastyTradeApi
import com.antyzero.tastytrade.core.network.TastyTradeAuthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    @Named("auth")
    fun provideAuthOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    @Named("default")
    fun provideDefaultOkHttpClient(@Named("auth") okHttpClient: OkHttpClient, authentication: Authentication, tokenStorage: TokenStorage): OkHttpClient {
        return okHttpClient.newBuilder()
            .addInterceptor {
                it.proceed(it.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build())
            }
            .addInterceptor {
                val request = it.request().newBuilder().apply {
                    val token = tokenStorage.accessToken
                    if(token != null) {
                        header("Authorization", "Bearer $token")
                    }
                }.build()

                it.proceed(request)
            }
            // This will be used when we get 401
            .authenticator { _, response ->
                // It's blocking but per my research OkHttpClient is fine with blocking calls here
                val token = runBlocking { authentication.refreshAccessToken() }

                val request = response.request.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()

                request
            }
            .build()
    }

    @Provides
    @Singleton
    @Named("auth")
    fun provideAuthRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(SANDBOX_BASE_URL)
            .addConverterFactory(
                Json.asConverterFactory(
                    "application/json; charset=UTF8".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    @Named("default")
    fun provideDefaultRetrofit(@Named("default") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(SANDBOX_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                Json.asConverterFactory(
                    "application/json; charset=UTF8".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun provideTastyTradeAuthApi(@Named("auth") retrofit: Retrofit): TastyTradeAuthApi {
        return retrofit.create(TastyTradeAuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTastyTradeApi(@Named("default") retrofit: Retrofit): TastyTradeApi {
        return retrofit.create(TastyTradeApi::class.java)
    }
}