package com.example.pricetracker.presentation.feed

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pricetracker.data.StockRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@Stable
class FeedVM(
    private val repository: StockRepository
) : ViewModel() {

    val isLiveUpdateEnabled = mutableStateOf(true)

    val state: StateFlow<StockFeedState> = combine(
        repository.stockFeed,
        repository.connectionStatus
    ) { stocks, isConnected ->

        StockFeedState(
            stocks = stocks,
            isConnected = isConnected,
            isLoading = false,
            error = null
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = StockFeedState(isLoading = true)
    )

    fun toggleFeed() {

        if (isLiveUpdateEnabled.value) repository.connectFeed() else repository.disconnectFeed()
    }

    fun disconnectFeed() {
        repository.disconnectFeed()
    }
}