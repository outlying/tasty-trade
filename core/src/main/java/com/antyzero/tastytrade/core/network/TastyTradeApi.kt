package com.antyzero.tastytrade.core.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET


@Serializable
data class InstrumentsCryptocurrenciesResponse(
    val data: InstrumentsCryptocurrenciesData,
    val context: String
)

@Serializable
data class InstrumentsCryptocurrenciesData(
    val items: List<CryptoInstrument>
)

@Serializable
data class CryptoInstrument(
    val id: Int,
    val active: Boolean,
    val description: String,
    @SerialName("instrument-type") val instrumentType: String,
    @SerialName("is-closing-only") val isClosingOnly: Boolean,
    @SerialName("short-description") val shortDescription: String,
    @SerialName("streamer-symbol") val streamerSymbol: String,
    val symbol: String,
    @SerialName("tick-size") val tickSize: String, // looks float but response is String type
    @SerialName("destination-venue-symbols") val destinationVenueSymbols: List<DestinationVenueSymbol>
)

@Serializable
data class DestinationVenueSymbol(
    val id: Int,
    @SerialName("destination-venue") val destinationVenue: String,
    // Precision fields are nor always present
    @SerialName("max-price-precision") val maxPricePrecision: Int? = null,
    @SerialName("max-quantity-precision") val maxQuantityPrecision: Int? = null,
    val routable: Boolean,
    val symbol: String
)


interface TastyTradeApi {

    @GET("instruments/cryptocurrencies")
    suspend fun getInstrumentsCryptocurrencies(): InstrumentsCryptocurrenciesResponse
}