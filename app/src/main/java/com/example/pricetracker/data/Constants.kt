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
            "AAPL" -> "Apple Inc. - Technology and consumer electronics."
            "GOOG" -> "Alphabet Inc. (Class C) - Global technology giant."
            "NVDA" -> "NVIDIA Corporation - Leader in AI and GPU technology."
            "TSLA" -> "Tesla, Inc. - Electric vehicle and clean energy leader."
            "MSFT" -> "Microsoft Corporation - Software and cloud computing."
            "AMZN" -> "Amazon.com, Inc. - E-commerce and cloud services (AWS)."
            "JPM" -> "JPMorgan Chase & Co. - Global financial services."
            "V" -> "Visa Inc. - Global payment technology company."
            "JNJ" -> "Johnson & Johnson - Pharmaceutical and medical devices."
            "WMT" -> "Walmart Inc. - Multinational retail corporation."
            "PG" -> "Procter & Gamble Co. - Consumer goods conglomerate."
            "MA" -> "Mastercard Incorporated - Global payment technology."
            "UNH" -> "UnitedHealth Group Incorporated - Healthcare and insurance."
            "HD" -> "The Home Depot, Inc. - Home improvement retail."
            "DIS" -> "The Walt Disney Company - Media and entertainment."
            "CRM" -> "Salesforce, Inc. - Customer relationship management software."
            "NFLX" -> "Netflix, Inc. - Streaming entertainment service."
            "ADBE" -> "Adobe Inc. - Creative and digital experience software."
            "PYPL" -> "PayPal Holdings, Inc. - Online payment systems."
            "CMCSA" -> "Comcast Corporation - Telecommunications and media."
            "INTC" -> "Intel Corporation - Semiconductor chips and technology."
            "CSCO" -> "Cisco Systems, Inc. - Networking hardware and software."
            "PEP" -> "PepsiCo, Inc. - Beverage and snack food company."
            "KO" -> "The Coca-Cola Company - Beverage corporation."
            "MCD" -> "McDonald's Corporation - Global fast-food chain."
            else -> "A leading company in the global market."
        }
    }
}