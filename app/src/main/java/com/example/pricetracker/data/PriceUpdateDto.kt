package com.example.pricetracker.data

import kotlinx.serialization.Serializable

@Serializable
data class PriceUpdateDto(
    val symbol: String,
    val price: Double
)