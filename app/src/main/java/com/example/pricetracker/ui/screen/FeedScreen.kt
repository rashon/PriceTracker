package com.example.pricetracker.ui.screen

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pricetracker.presentation.feed.FeedVM
import com.example.pricetracker.ui.composable.StockListItem

@Composable
fun FeedScreen(
    viewModel: FeedVM,
    onStockClick: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()

    LazyColumn(contentPadding = PaddingValues(8.dp)) {
        items(items = state.stocks, key = { stock -> stock.symbol }) { stock ->

            StockListItem(
                stockItem = stock,
                onClick = { onStockClick(stock.symbol) },
                modifier = Modifier.animateItem(tween(durationMillis = 250))
            )
        }
    }
}