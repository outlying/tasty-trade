package com.antyzero.tastytrade.core.network

import retrofit2.http.POST
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.Body

@Serializable
data class AccessTokenRequest(
    @SerialName("grant_type") val grantType: String,
    @SerialName("code") val code: String,
    @SerialName("client_id") val clientId: String,
    @SerialName("client_secret") val clientSecret: String,
    @SerialName("redirect_uri") val redirectUri: String
)

@Serializable
data class RefreshTokenRequest(
    @SerialName("grant_type") val grantType: String,
    @SerialName("refresh_token") val refreshToken: String,
    @SerialName("client_secret") val clientSecret: String
)

@Serializable
data class TokenResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String,
    @SerialName("token_type") val tokenType: String,
    @SerialName("expires_in") val expiresIn: Long,
    @SerialName("id_token") val idToken: String? = null
)

@Serializable
data class RefreshResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("expires_in") val expiresIn: Long,
    @SerialName("token_type") val tokenType: String,
)

interface TastyTradeAuthApi {

    @POST("oauth/token")
    suspend fun requestAccessToken(
        @Body request: AccessTokenRequest
    ): TokenResponse

    @POST("oauth/token")
    suspend fun refreshAccessToken(
        @Body request: RefreshTokenRequest
    ): RefreshResponse
}