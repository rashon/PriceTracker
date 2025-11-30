package com.example.pricetracker.data

import com.example.pricetracker.domain.model.StockItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface StockRepository {

    val connectionStatus: StateFlow<Boolean>

    val stockFeed: Flow<List<StockItem>>

    fun connectFeed()

    fun disconnectFeed()

    fun getStockBySymbol(symbol: String): Flow<StockItem?>
}