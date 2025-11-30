package com.example.pricetracker.presentation.detail

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pricetracker.data.StockRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


@Stable
class DetailVM(
    private val repository: StockRepository,
    private val symbol: String
) : ViewModel() {

    val detailsState: StateFlow<StockDetailState> = repository.getStockBySymbol(symbol)

        .map { stockItem ->

            if (stockItem != null) {

                StockDetailState(
                    isLoading = false,
                    stock = stockItem,
                    error = null
                )
            } else {

                StockDetailState(
                    isLoading = false,
                    stock = null,
                    error = "Stock details not found for $symbol."
                )
            }
        }

        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = StockDetailState(isLoading = true)
        )
}