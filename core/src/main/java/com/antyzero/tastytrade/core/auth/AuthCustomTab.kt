package com.antyzero.tastytrade.core.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

fun launchAuthCustomTab(context: Context, uri: Uri) {

    val customTabsIntent = CustomTabsIntent.Builder()
            .build()

    customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
    customTabsIntent.launchUrl(context, uri)
}