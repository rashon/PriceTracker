package com.example.pricetracker.data

import com.example.pricetracker.domain.model.StockItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale


class StockRepositoryImpl(
    private val client: WebSocketClient,
    private val scope: CoroutineScope
) : StockRepository {

    private val initialStocks: List<StockItem> by lazy {

        StockConstants.SYMBOL_TICKERS.mapIndexed { index, symbol ->

            // Base price calculation to make some stocks high-priced and others low-priced
            val basePrice = 100.0 + (index * 15.0) + (kotlin.random.Random.nextDouble() * 50.0)

            StockItem(
                symbol = symbol,
                currentPrice = String.format(Locale.US, "%.2f", basePrice).toDouble(),
                lastChange = 0.0,
                description = StockConstants.getDescription(symbol),
                name = StockConstants.getName(symbol)
            )
        }
    }

    private val _stockUpdates = MutableStateFlow(initialStocks)

    private val _connectionStatus = MutableStateFlow(false)

    override val connectionStatus: StateFlow<Boolean> = _connectionStatus

    override val stockFeed: Flow<List<StockItem>> = _stockUpdates
        .map { list ->
            list.sortedByDescending { it.currentPrice }
        }

    init {

        scope.launch {
            client.stockUpdates.collect { updatedList ->

                _stockUpdates.value = updatedList

                _connectionStatus.update { true }
            }
        }

        startFeed()
    }

    override fun connectFeed() {

        startFeed()

        _connectionStatus.value = true
    }

    override fun disconnectFeed() {

        client.disconnect()

        _connectionStatus.value = false
    }

    private fun startFeed() {

        if (connectionStatus.value) return // Already running

        client.connect(_stockUpdates.value)
    }

    override fun getStockBySymbol(symbol: String): Flow<StockItem?> {

        return _stockUpdates
            .map { list ->
                list.find { it.symbol.equals(symbol, ignoreCase = true) }
            }
    }
}