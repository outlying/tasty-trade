package com.antyzero.tastytrade.feature.instrumentscryptocurrencies.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.antyzero.tastytrade.core.repository.InstrumentsCryptocurrenciesDomain
import com.antyzero.tastytrade.core.ui.MyApplicationTheme
import com.antyzero.tastytrade.feature.instrumentscryptocurrencies.ui.InstrumentsCryptocurrenciesUiState.Error
import com.antyzero.tastytrade.feature.instrumentscryptocurrencies.ui.InstrumentsCryptocurrenciesUiState.Loading
import com.antyzero.tastytrade.feature.instrumentscryptocurrencies.ui.InstrumentsCryptocurrenciesUiState.Success

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstrumentsCryptocurrenciesScreen(modifier: Modifier = Modifier, viewModel: InstrumentsCryptocurrenciesViewModel = hiltViewModel()) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isRefreshing = uiState is Loading

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("WatchList") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(
                        onClick = { viewModel.refresh() },
                        enabled = !isRefreshing
                    ) {
                        if (isRefreshing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = "Refresh"
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->

        when(uiState) {

            is Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(96.dp),
                        strokeWidth = 8.dp
                    )
                }
            }

            is Success -> {
                InstrumentsCryptocurrenciesScreen(
                    items = (uiState as Success).data,
                    modifier = modifier.padding(innerPadding)
                )
            }

            is Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Data loading failed")
                }
            }
        }
    }
}

@Composable
internal fun InstrumentsCryptocurrenciesScreen(
    items: List<InstrumentsCryptocurrenciesDomain>,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        items.forEach {
            Text(modifier = Modifier.padding(16.dp), text = it.name)
        }
    }
}

// Previews

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        InstrumentsCryptocurrenciesScreen(listOf("Compose", "Room", "Kotlin").map { InstrumentsCryptocurrenciesDomain(it) })
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    MyApplicationTheme {
        InstrumentsCryptocurrenciesScreen(listOf("Compose", "Room", "Kotlin").map { InstrumentsCryptocurrenciesDomain(it) })
    }
}
