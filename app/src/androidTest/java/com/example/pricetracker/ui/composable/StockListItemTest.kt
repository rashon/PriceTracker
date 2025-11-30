package com.example.pricetracker.ui.composable

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.pricetracker.domain.model.StockItem
import org.junit.Rule
import org.junit.Test

class StockListItemTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun stockListItem_displaysCorrectInfo() {
        val stock = StockItem(
            symbol = "AAPL",
            name = "Apple Inc.",
            currentPrice = 150.50,
            lastChange = 1.5,
            description = "Tech giant",
        )

        composeTestRule.setContent {
            StockListItem(
                stockItem = stock,
                onClick = {}
            )
        }

        composeTestRule.onNodeWithText("AAPL").assertIsDisplayed()
        composeTestRule.onNodeWithText("Apple Inc.").assertIsDisplayed()
        composeTestRule.onNodeWithText("$150.50").assertIsDisplayed()
        composeTestRule.onNodeWithText("1.50 (1.01%)").assertIsDisplayed()
    }

    @Test
    fun stockListItem_onClickTriggered() {
        var clickedItem: StockItem? = null
        val stock = StockItem(
            symbol = "GOOGL",
            name = "Google",
            currentPrice = 2800.0,
            lastChange = -5.0,
            description = "Search engine",
        )

        composeTestRule.setContent {
            StockListItem(
                stockItem = stock,
                onClick = { clickedItem = it }
            )
        }

        composeTestRule.onNodeWithText("GOOGL").performClick()
        assert(clickedItem == stock)
    }
}