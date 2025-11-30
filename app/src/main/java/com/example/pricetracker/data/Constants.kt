package com.example.pricetracker.data


object StockConstants {
    val SYMBOL_TICKERS = listOf(
        "AAPL", "GOOG", "TSLA", "AMZN", "MSFT",
        "NVDA", "JPM", "V", "JNJ", "WMT",
        "PG", "MA", "UNH", "HD", "DIS",
        "CRM", "NFLX", "ADBE", "PYPL", "CMCSA",
        "INTC", "CSCO", "PEP", "KO", "MCD"
    )

    fun getDescription(symbol: String): String {
        return when (symbol) {
            "AAPL" -> "Technology and consumer electronics."
            "GOOG" -> "Global technology giant."
            "NVDA" -> "Leader in AI and GPU technology."
            "TSLA" -> "Electric vehicle and clean energy leader."
            "MSFT" -> "Software and cloud computing."
            "AMZN" -> "E-commerce and cloud services (AWS)."
            "JPM" -> "Global financial services."
            "V" -> "Global payment technology company."
            "JNJ" -> "Pharmaceutical and medical devices."
            "WMT" -> "Multinational retail corporation."
            "PG" -> "Consumer goods conglomerate."
            "MA" -> "Global payment technology."
            "UNH" -> "Healthcare and insurance."
            "HD" -> "Home improvement retail."
            "DIS" -> "Media and entertainment."
            "CRM" -> "Customer relationship management software."
            "NFLX" -> "Streaming entertainment service."
            "ADBE" -> "Creative and digital experience software."
            "PYPL" -> "Online payment systems."
            "CMCSA" -> "Telecommunications and media."
            "INTC" -> "Semiconductor chips and technology."
            "CSCO" -> "Networking hardware and software."
            "PEP" -> "Beverage and snack food company."
            "KO" -> "Beverage corporation."
            "MCD" -> "Global fast-food chain."
            else -> "A leading company in the global market."
        }
    }

    fun getName(symbol: String): String {
        return when (symbol) {
            "AAPL" -> "Apple Inc"
            "GOOG" -> "Alphabet Inc"
            "NVDA" -> "NVIDIA Corporation"
            "TSLA" -> "Tesla, Inc."
            "MSFT" -> "Microsoft Corporation"
            "AMZN" -> "Amazon.com, Inc."
            "JPM" -> "JPMorgan Chase & Co."
            "V" -> "Visa Inc."
            "JNJ" -> "Johnson & Johnson"
            "WMT" -> "Walmart Inc."
            "PG" -> "Procter & Gamble Co."
            "MA" -> "Mastercard Incorporated"
            "UNH" -> "UnitedHealth Group Incorporated"
            "HD" -> "The Home Depot, Inc."
            "DIS" -> "The Walt Disney Company"
            "CRM" -> "Salesforce, Inc."
            "NFLX" -> "Netflix, Inc."
            "ADBE" -> "Adobe Inc."
            "PYPL" -> "PayPal Holdings, Inc."
            "CMCSA" -> "Comcast Corporation"
            "INTC" -> "Intel Corporation"
            "CSCO" -> "Cisco Systems, Inc."
            "PEP" -> "PepsiCo, Inc."
            "KO" -> "The Coca-Cola Company"
            "MCD" -> "McDonald's Corporation"
            else -> "Unknown Company"
        }
    }
}