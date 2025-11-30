package com.example.pricetracker.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pricetracker.presentation.detail.DetailVM
import com.example.pricetracker.presentation.feed.FeedVM
import com.example.pricetracker.ui.composable.TopAppBar
import com.example.pricetracker.ui.screen.DetailScreen
import com.example.pricetracker.ui.screen.FeedScreen
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf


@Composable
fun PriceTrackerAppNavHost(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val feedVM: FeedVM = koinViewModel()
    val feedState by feedVM.state.collectAsState()

    val detailSymbol =
        navBackStackEntry?.arguments?.getString(AppDestinations.DetailScreen.SYMBOL_KEY)

    Scaffold(
        topBar = {
            TopAppBar(
                currentRoute = currentRoute,
                navController = navController,
                symbol = detailSymbol,
                isLiveFeedEnabled = feedVM.isLiveUpdateEnabled.value,
                onToggleLiveFeed = { feedVM.toggleFeed(it) },
                serverStatus = feedState.isConnected
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppDestinations.FeedScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {

            // --- FeedScreen (Start Destination) ---
            composable(route = AppDestinations.FeedScreen.route) {
                FeedScreen(
                    viewModel = feedVM,
                    onStockClick = { symbol ->
                        navController.navigate(AppDestinations.DetailScreen.createRoute(symbol))
                    }
                )
            }

            // --- DetailScreen ---
            composable(
                route = AppDestinations.DetailScreen.route,
                arguments = listOf(navArgument(AppDestinations.DetailScreen.SYMBOL_KEY) {
                    type = NavType.StringType
                })
            ) { backStackEntry ->
                val symbol =
                    backStackEntry.arguments?.getString(AppDestinations.DetailScreen.SYMBOL_KEY)
                        ?: return@composable

                val detailViewModel: DetailVM = koinViewModel(parameters = { parametersOf(symbol) })

                DetailScreen(viewModel = detailViewModel)
            }
        }
    }
}