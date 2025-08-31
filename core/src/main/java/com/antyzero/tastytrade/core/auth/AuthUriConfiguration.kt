package com.antyzero.tastytrade.core.auth

data class AuthUriConfiguration(
    val schema: String = "https",
    val authority: String,
    val path: String,
    val queryParams: List<Pair<String, String>> = emptyList()
) {

    fun getQueryParam(key: String) = queryParams.toMap()[key]

    companion object {
        const val QUERY_CLIENT_ID = "client_id"
        const val QUERY_REDIRECT_URI = "redirect_uri"
        const val QUERY_RESPONSE_TYPE = "response_type"
        const val QUERY_SCOPE = "scope"
    }
}

