package com.antyzero.tastytrade.core.auth

import android.net.Uri
import kotlin.jvm.Throws

interface AuthUriProvider {

    fun loginUri(): Uri

    @Throws(IllegalAccessException::class)
    fun redirectUri(): String

    class Factory(private val configuration: AuthUriConfiguration): AuthUriProvider {

        override fun loginUri(): Uri {

            return Uri.Builder()
                .scheme(configuration.schema)
                .authority(configuration.authority)
                .path(configuration.path)
                .apply {
                    for((key, value) in configuration.queryParams) {
                        appendQueryParameter(key, value)
                    }
                }
                .build()
        }

        override fun redirectUri(): String = configuration.getQueryParam(AuthUriConfiguration.QUERY_REDIRECT_URI)
            ?: throw IllegalAccessException("Missing redirect URI")
    }
}