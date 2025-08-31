package com.antyzero.tastytrade.feature.login.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.antyzero.tastytrade.core.ui.MyApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLogin: () -> Unit,
    onIsAuthenticated: () -> Unit,
) {
    val latestOnIsAuthenticated = rememberUpdatedState(onIsAuthenticated)

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when(effect) {
                LoginViewModel.AuthenticationEffect.IsAuthenticated -> {
                    latestOnIsAuthenticated.value()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("WatchList Login") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
            )
        },
        contentWindowInsets = WindowInsets(0)
    ) { innerPadding ->

        LoginScreenInternal(
            innerPadding,
            onLogin
        )
    }
}

@Composable
internal fun LoginScreenInternal(
    innerPadding: PaddingValues,
    onLogin: () -> Unit
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

            Button(
                onClick = { onLogin.invoke() },
                enabled = true,
                modifier = Modifier
                    .heightIn(min = 48.dp)
            ) {
                Text("OAuth2 browser login")
            }
        }
    }
}

// Previews

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        LoginScreen(
            onLogin = {},
            onIsAuthenticated = {}
        )
    }
}