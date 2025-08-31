package com.antyzero.tastytrade.core.auth

import com.antyzero.tastytrade.core.network.AccessTokenRequest
import com.antyzero.tastytrade.core.network.RefreshTokenRequest
import com.antyzero.tastytrade.core.network.TastyTradeAuthApi
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CancellationException
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

private val logger = KotlinLogging.logger {  }

interface Authentication {

    suspend fun exchangeCodeForAccessToken(code: String)

    suspend fun refreshAccessToken(): String?

    suspend fun isAuthenticated(): Boolean
}

@OptIn(ExperimentalTime::class)
class StandardAuthentication(
    private val configuration: AuthConfiguration,
    private val tokenStorage: TokenStorage,
    private val tastyTradeAuthApi: TastyTradeAuthApi
): Authentication {

    override suspend fun exchangeCodeForAccessToken(code: String) {

        val codeExchangePayload = AccessTokenRequest(
            code = code,
            clientId = configuration.clientId,
            clientSecret = configuration.clientSecret,
            redirectUri = configuration.redirectUri,
            grantType = "authorization_code"
        )

        try {
            val tokenResponse = tastyTradeAuthApi.requestAccessToken(codeExchangePayload)
            val secondsToExpire = tokenResponse.expiresIn

            tokenStorage.updateTokens(
                accessToken = tokenResponse.accessToken,
                expiresIn = secondsToExpire,
                refreshToken = tokenResponse.refreshToken
            )
            logger.debug { "Token will expire in ${secondsToExpire} seconds" }
        } catch (ce: CancellationException) {
            throw ce
        } catch (e: retrofit2.HttpException) {
            logger.warn { "Network issue ${e.response()?.errorBody()?.string()}" }
            throw IllegalStateException("Network error")
        }
    }

    override suspend fun refreshAccessToken(): String? {

        val refreshTokenRequest = RefreshTokenRequest(
            grantType = "refresh_token",
            clientSecret = configuration.clientSecret,
            refreshToken = tokenStorage.refreshToken ?: run {
                logger.error { "Unable to obtain refresh token from storage" }
                throw IllegalStateException("Missing refresh token")
            }
        )

        try {
            val refreshResponse = tastyTradeAuthApi.refreshAccessToken(refreshTokenRequest)

            tokenStorage.updateTokens(
                accessToken = refreshResponse.accessToken,
                expiresIn = refreshResponse.expiresIn,
            )

            return refreshResponse.accessToken
        } catch (ce: CancellationException) {
            throw ce
        }
    }

    /**
     * It is simplification, but if we have refresh token then we successfully get it in the past
     * and even if access token is expired we can easily refresh it
     */
    override suspend fun isAuthenticated(): Boolean {
        val refreshToken = tokenStorage.refreshToken
        return refreshToken != null
    }

    private fun isAccessTokenExpired(): Boolean {
        val expiryEpochSeconds = tokenStorage.expiryEpochSeconds
            ?: run {
                logger.error { "Expiry seconds are not available, storage was not populated" }
                throw IllegalStateException("Token was never obtained")
            }
        return Clock.System.now().epochSeconds >= expiryEpochSeconds
    }
}