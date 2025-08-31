package com.antyzero.tastytrade.core.auth

data class AuthConfiguration(
    val clientId: String,
    val clientSecret: String,
    val redirectUri: String
)
