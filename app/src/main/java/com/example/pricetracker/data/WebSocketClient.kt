package com.example.pricetracker.data

import com.example.pricetracker.domain.model.StockItem
import kotlinx.coroutines.flow.SharedFlow

interface WebSocketClient {
    val stockUpdates: SharedFlow<List<StockItem>>
    fun connect(initialStocks: List<StockItem>)
    fun disconnect()
}