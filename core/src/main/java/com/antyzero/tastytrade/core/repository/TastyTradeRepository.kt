package com.antyzero.tastytrade.core.repository

import com.antyzero.tastytrade.core.network.TastyTradeApi
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.HttpException
import javax.inject.Inject

data class InstrumentsCryptocurrenciesDomain(
    val name: String
)

// Some alternative to Result to avoid nullability in Result - instrumentsCryptocurrencies
sealed class Data<T> {
    object Uninitialized
    data class Value<T>(val value: T)
    class Error(e: Throwable)
}

interface TastyTradeRepository {

    suspend fun refreshInstrumentsCryptocurrencies()

    val instrumentsCryptocurrencies: Flow<Result<List<InstrumentsCryptocurrenciesDomain>?>>
}

class DefaultTastyTradeRepository @Inject constructor(
    private val tastyTradeApi: TastyTradeApi
) : TastyTradeRepository {

    private val logger = KotlinLogging.logger {  }

    private val _instrumentsCryptocurrencies =
        MutableStateFlow<Result<List<InstrumentsCryptocurrenciesDomain>?>>(Result.success(null))

    override suspend fun refreshInstrumentsCryptocurrencies() {

        val response = try {
            tastyTradeApi.getInstrumentsCryptocurrencies()
        } catch (ce: CancellationException) {
            throw ce
        } catch (e: HttpException) {
            logger.error { "Network error, ${e.response()?.errorBody()?.string()}" }
            _instrumentsCryptocurrencies.value = Result.failure(e)
            return
        }

        _instrumentsCryptocurrencies.value = Result.success(
            response.data.items.map {
                InstrumentsCryptocurrenciesDomain(it.shortDescription)
            }
        )
    }

    override val instrumentsCryptocurrencies: Flow<Result<List<InstrumentsCryptocurrenciesDomain>?>> =
        _instrumentsCryptocurrencies.asStateFlow()
}
