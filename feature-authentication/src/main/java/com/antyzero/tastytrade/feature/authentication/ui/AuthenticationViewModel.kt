package com.antyzero.tastytrade.feature.authentication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antyzero.tastytrade.core.auth.Authentication
import com.antyzero.tastytrade.feature.authentication.ui.AuthenticationUiState.Error
import com.antyzero.tastytrade.feature.authentication.ui.AuthenticationUiState.Loading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authentication: Authentication
) : ViewModel() {

    sealed interface AuthenticationEffect {
        data object NavigateHome : AuthenticationEffect
    }

    private val _uiState = MutableStateFlow<AuthenticationUiState>(Loading)
    val uiState: StateFlow<AuthenticationUiState> = _uiState

    private val _effects = MutableSharedFlow<AuthenticationEffect>(extraBufferCapacity = 1)
    val effects: SharedFlow<AuthenticationEffect> = _effects.asSharedFlow()

    fun processCode(code: String) {
        viewModelScope.launch {

            try {
                _uiState.value = AuthenticationUiState.FetchingAuthToken
                authentication.exchangeCodeForAccessToken(code)
                _uiState.value = AuthenticationUiState.Success
                _effects.tryEmit(AuthenticationEffect.NavigateHome)
            } catch (ce: CancellationException) {
                throw ce
            } catch (e: IOException) {
                _uiState.value = Error("Network issue", e)
            }
        }
    }
}

sealed interface AuthenticationUiState {
    object Loading : AuthenticationUiState
    data object FetchingAuthToken: AuthenticationUiState
    data object Success : AuthenticationUiState
    data class Error(val message: String, val throwable: Throwable) : AuthenticationUiState
}
