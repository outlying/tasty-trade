package com.antyzero.tastytrade.feature.authentication.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.antyzero.tastytrade.core.ui.MyApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticationScreen(
    viewModel: AuthenticationViewModel,
    onAuthenticated: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val latestOnAuth = rememberUpdatedState(onAuthenticated)

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                AuthenticationViewModel.AuthenticationEffect.NavigateHome ->
                    latestOnAuth.value() // triggers callback
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("OAuth") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
            )
        },
        contentWindowInsets = WindowInsets(0)
    ) { innerPadding ->
        AuthenticationScreen(
            innerPadding,
            state
        )
    }
}

@Composable
internal fun AuthenticationScreen(
    innerPadding: PaddingValues,
    state: AuthenticationUiState
) {

    Box(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .widthIn(max = 420.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            CircularProgressIndicator(
                modifier = Modifier.size(96.dp),
                strokeWidth = 8.dp
            )
            Spacer(Modifier.height(16.dp))

            Text(when(state) {
                AuthenticationUiState.Loading -> "Starting token exchange"
                AuthenticationUiState.FetchingAuthToken -> "Fetching authentication token"
                is AuthenticationUiState.Error -> "Error: ${state.throwable.message}"
                AuthenticationUiState.Success -> {
                    "Authentication token obtained"
                }
            })
        }
    }
}

// Previews

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        AuthenticationScreen(
            innerPadding = PaddingValues(0.dp),
            state = AuthenticationUiState.Loading
        )
    }
}