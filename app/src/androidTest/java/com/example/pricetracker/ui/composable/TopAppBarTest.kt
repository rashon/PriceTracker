package com.example.pricetracker.ui.composable

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import com.example.pricetracker.presentation.navigation.AppDestinations
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class TopAppBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun topAppBar_displaysTitle_onDetailScreen() {
        val navController = mockk<NavHostController>(relaxed = true)
        
        composeTestRule.setContent {
            TopAppBar(
                currentRoute = AppDestinations.DetailScreen.route,
                navController = navController,
                symbol = "AAPL",
                isLiveFeedEnabled = false,
                onToggleLiveFeed = {},
                serverStatus = true
            )
        }

        composeTestRule.onNodeWithText("AAPL").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Back").assertIsDisplayed()
    }

    @Test
    fun topAppBar_displaysStatus_onFeedScreen() {
        val navController = mockk<NavHostController>(relaxed = true)

        composeTestRule.setContent {
            TopAppBar(
                currentRoute = AppDestinations.FeedScreen.route,
                navController = navController,
                symbol = null,
                isLiveFeedEnabled = true,
                onToggleLiveFeed = {},
                serverStatus = true
            )
        }

        composeTestRule.onNodeWithContentDescription("Server Status").assertIsDisplayed()
    }

    @Test
    fun topAppBar_backButton_navigatesBack() {
        val navController = mockk<NavHostController>(relaxed = true)

        composeTestRule.setContent {
            TopAppBar(
                currentRoute = AppDestinations.DetailScreen.route,
                navController = navController,
                symbol = "AAPL",
                isLiveFeedEnabled = false,
                onToggleLiveFeed = {},
                serverStatus = true
            )
        }

        composeTestRule.onNodeWithContentDescription("Back").performClick()
        
        verify { navController.popBackStack() }
    }
}
