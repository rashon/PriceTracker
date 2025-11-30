package com.example.pricetracker.domain.model


/**
 * @property symbol The unique stock ticker (e.g., "AAPL", "NVDA").
 * @property currentPrice The latest price value.
 * @property lastChange The difference between the current price and the previous one.
 * @property description A static description for the symbol details screen.
 */
data class StockItem(
    val symbol: String,
    val name: String,
    val currentPrice: Double,
    val lastChange: Double,
    val description: String,
    val timestamp: Long = System.currentTimeMillis()
) {
    /**
     * Calculates the price change direction based on the lastChange value.
     */
    val changeDirection: ChangeDirection
        get() = when {
            lastChange > 0 -> ChangeDirection.UP
            lastChange < 0 -> ChangeDirection.DOWN
            else -> ChangeDirection.NONE
        }
}
