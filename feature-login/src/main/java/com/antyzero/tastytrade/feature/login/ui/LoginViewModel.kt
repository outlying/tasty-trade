package com.antyzero.tastytrade.feature.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antyzero.tastytrade.core.auth.Authentication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authentication: Authentication
) : ViewModel() {

    sealed interface AuthenticationEffect {
        data object IsAuthenticated : AuthenticationEffect
    }

    private val _effects = MutableSharedFlow<AuthenticationEffect>(replay = 1)
    val effects: SharedFlow<AuthenticationEffect> = _effects.asSharedFlow()

    init {
        viewModelScope.launch {
            if(authentication.isAuthenticated()) {
                _effects.tryEmit(AuthenticationEffect.IsAuthenticated)
            }
        }
    }
}

sealed interface WatchlistUiState {
    object Loading : WatchlistUiState
    data class Error(val throwable: Throwable) : WatchlistUiState
    data class Success(val data: List<String>) : WatchlistUiState
}
