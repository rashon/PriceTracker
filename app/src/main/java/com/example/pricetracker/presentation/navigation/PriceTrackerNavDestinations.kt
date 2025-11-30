package com.example.pricetracker.presentation.navigation

sealed class AppDestinations(val route: String) {

    data object FeedScreen : AppDestinations("feed_screen")

    data object DetailScreen : AppDestinations("detail_screen/{symbol}") {

        fun createRoute(symbol: String) = "detail_screen/$symbol"

        const val SYMBOL_KEY = "symbol"
    }
}