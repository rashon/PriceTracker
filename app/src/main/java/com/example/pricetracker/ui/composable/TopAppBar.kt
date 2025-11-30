package com.example.pricetracker.ui.composable

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.pricetracker.presentation.navigation.AppDestinations
import com.example.pricetracker.ui.theme.greenIndicator
import com.example.pricetracker.ui.theme.redIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    currentRoute: String?,
    navController: NavHostController,
    symbol: String?,
    isLiveFeedEnabled: Boolean,
    onToggleLiveFeed: (Boolean) -> Unit,
    serverStatus: Boolean
) {

    val detailTitle = symbol ?: "Details"

    val actions: @Composable RowScope.() -> Unit = {

        Switch(
            checked = isLiveFeedEnabled,
            onCheckedChange = { onToggleLiveFeed(it) },

            modifier = Modifier.padding(end = 8.dp)
        )
    }

    CenterAlignedTopAppBar(

        title = {
            if (currentRoute == AppDestinations.DetailScreen.route) {
                Text(detailTitle)
            } else {
                Text("")
            }
        },

        navigationIcon = {
            when (currentRoute) {

                AppDestinations.DetailScreen.route -> {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }

                AppDestinations.FeedScreen.route -> {
                    val color = if (serverStatus) greenIndicator else redIndicator
                    Icon(
                        imageVector = Icons.Default.Circle,
                        contentDescription = "Server Status",
                        tint = color,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                else -> { /* No icon */
                }
            }
        },
        actions = actions
    )
}