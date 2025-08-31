package com.antyzero.tastytrade.core.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

interface TokenStorage {
    var accessToken: String?
    var refreshToken: String?
    var expiryEpochSeconds: Long?
}

/**
 * Cutting corners big time here TODO:
 * This should be implemented to use some sort of encryption/decryption mechanism to slow down
 * potential attacker
 *
 * ... but as the time of writing code I discovered that EncryptedSharedPreferences is deprecated
 *
 * so note, this is not recommended for production but sandbox is fine for it
 */
class SharedPrefsTokenStorage(context: Context) : TokenStorage {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)

    override var accessToken: String?
        get() = prefs.getString(KEY_ACCESS, null)
        set(value) {
            prefs.edit {
                if (value == null) remove(KEY_ACCESS) else putString(KEY_ACCESS, value)
            }
        }

    override var refreshToken: String?
        get() = prefs.getString(KEY_REFRESH, null)
        set(value) {
            prefs.edit {
                if (value == null) remove(KEY_REFRESH) else putString(KEY_REFRESH, value)
            }
        }

    override var expiryEpochSeconds: Long?
        get() = if (prefs.contains(KEY_EXPIRY)) prefs.getLong(KEY_EXPIRY, 0L) else null
        set(value) {
            prefs.edit {
                if (value == null) remove(KEY_EXPIRY) else putLong(KEY_EXPIRY, value)
            }
        }

    private companion object {
        const val PREFS_FILE = "token_storage_prefs"
        const val KEY_ACCESS = "access_token"
        const val KEY_REFRESH = "refresh_token"
        const val KEY_EXPIRY = "expiry_epoch_seconds"
    }
}

@OptIn(ExperimentalTime::class)
fun TokenStorage.updateTokens(
    accessToken: String,
    expiresIn: Long,
    refreshToken: String? = null
) {
    val now = Clock.System.now().epochSeconds
    this.accessToken = accessToken
    refreshToken?.let { this.refreshToken = it }
    // We reduce it a little to not update in the last possible moment
    val inFuture = now + (expiresIn - 30L).coerceAtLeast(0L)
    expiryEpochSeconds = inFuture
}