package com.example.pricetracker.presentation.feed

import com.example.pricetracker.domain.model.StockItem

data class StockFeedState(
    val stocks: List<StockItem> = emptyList(),
    val isConnected: Boolean = false,
    val isLoading: Boolean = true,
    val error: String? = null
)