package com.example.pricetracker.ui.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.pricetracker.data.StockRepository
import com.example.pricetracker.domain.model.StockItem
import com.example.pricetracker.presentation.feed.FeedVM
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class FeedScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun feedScreen_displaysStockList() {
        val stocks = listOf(
            StockItem("AAPL", "Apple", 150.0, 1.0, "Tech"),
            StockItem("GOOGL", "Google", 2800.0, -5.0, "Tech")
        )

        val repository = mockk<StockRepository>(relaxed = true)
        every { repository.stockFeed } returns flowOf(stocks)
        every { repository.connectionStatus } returns MutableStateFlow(true)

        val viewModel = FeedVM(repository)

        composeTestRule.setContent {
            FeedScreen(
                viewModel = viewModel,
                onStockClick = {}
            )
        }

        composeTestRule.onNodeWithText("AAPL").assertIsDisplayed()
        composeTestRule.onNodeWithText("Google").assertIsDisplayed()
    }

    @Test
    fun feedScreen_handlesClicks() {
        var clickedSymbol = ""
        val stocks = listOf(StockItem("MSFT", "Microsoft", 300.0, 2.0, "Tech"))

        val repository = mockk<StockRepository>(relaxed = true)
        every { repository.stockFeed } returns flowOf(stocks)
        every { repository.connectionStatus } returns MutableStateFlow(true)

        val viewModel = FeedVM(repository)

        composeTestRule.setContent {
            FeedScreen(
                viewModel = viewModel,
                onStockClick = { clickedSymbol = it }
            )
        }

        composeTestRule.onNodeWithText("MSFT").performClick()
        assert(clickedSymbol == "MSFT")
    }
}
