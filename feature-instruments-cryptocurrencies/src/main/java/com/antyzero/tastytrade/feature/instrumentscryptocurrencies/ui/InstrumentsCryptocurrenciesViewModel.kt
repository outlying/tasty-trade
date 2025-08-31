package com.antyzero.tastytrade.feature.instrumentscryptocurrencies.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antyzero.tastytrade.core.repository.InstrumentsCryptocurrenciesDomain
import com.antyzero.tastytrade.core.repository.TastyTradeRepository
import com.antyzero.tastytrade.feature.instrumentscryptocurrencies.ui.InstrumentsCryptocurrenciesUiState.Error
import com.antyzero.tastytrade.feature.instrumentscryptocurrencies.ui.InstrumentsCryptocurrenciesUiState.Loading
import com.antyzero.tastytrade.feature.instrumentscryptocurrencies.ui.InstrumentsCryptocurrenciesUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * For this VM and Screen - I added this as extra to show that data is actually loading from
 * protected endpoint, it is very simple and minimal
 */
@HiltViewModel
class InstrumentsCryptocurrenciesViewModel @Inject constructor(
    private val tastyTradeRepository: TastyTradeRepository
) : ViewModel() {

    val uiState: StateFlow<InstrumentsCryptocurrenciesUiState> = tastyTradeRepository
        .instrumentsCryptocurrencies
        .filter { it.isSuccess && it.getOrNull() != null }
        .map<Result<List<InstrumentsCryptocurrenciesDomain>?>, InstrumentsCryptocurrenciesUiState> {
            Success(data = it.getOrNull() ?: emptyList())
        }
        .catch { emit(Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)


    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            tastyTradeRepository.refreshInstrumentsCryptocurrencies()
        }
    }
}

sealed interface InstrumentsCryptocurrenciesUiState {
    object Loading : InstrumentsCryptocurrenciesUiState
    data class Error(val throwable: Throwable) : InstrumentsCryptocurrenciesUiState
    data class Success(val data: List<InstrumentsCryptocurrenciesDomain>) :
        InstrumentsCryptocurrenciesUiState
}
