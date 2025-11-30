package com.example.pricetracker.presentation.detail

import com.example.pricetracker.domain.model.StockItem

data class StockDetailState(
    val isLoading: Boolean = true,
    val stock: StockItem? = null,
    val error: String? = null
)