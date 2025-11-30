package com.example.pricetracker.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pricetracker.domain.model.ChangeDirection
import com.example.pricetracker.domain.model.StockItem
import com.example.pricetracker.ui.theme.greenIndicator
import com.example.pricetracker.ui.theme.redIndicator
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun StockListItem(
    stockItem: StockItem,
    onClick: (StockItem) -> Unit,
    modifier: Modifier = Modifier
) {

    val defaultColor = MaterialTheme.colorScheme.onSurface

    val changeColor = when (stockItem.changeDirection) {
        ChangeDirection.UP -> greenIndicator
        ChangeDirection.DOWN -> redIndicator
        else -> defaultColor
    }

    var priceTextColor by remember { mutableStateOf(defaultColor) }

    LaunchedEffect(stockItem.currentPrice) {
        if (stockItem.changeDirection != ChangeDirection.NONE) {

            priceTextColor = changeColor

            delay(1000L)

            priceTextColor = defaultColor
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable { onClick(stockItem) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side: Symbol and Name
            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = stockItem.symbol,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = stockItem.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Right side: Price and Change
            Row(verticalAlignment = Alignment.CenterVertically) {

                if (stockItem.changeDirection != ChangeDirection.NONE) {
                    val icon = when (stockItem.changeDirection) {
                        ChangeDirection.UP -> Icons.Default.ArrowUpward
                        ChangeDirection.DOWN -> Icons.Default.ArrowDownward
                        else -> null
                    }

                    icon?.let {
                        Icon(
                            imageVector = it,
                            contentDescription = null,
                            tint = changeColor,
                            modifier = Modifier
                                .size(20.dp)
                                .padding(end = 2.dp)
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {

                    Text(
                        text = "$${
                            String.format(
                                Locale.US, // FIX: Explicitly use Locale.US
                                "%.2f",
                                stockItem.currentPrice
                            )
                        }",
                        style = MaterialTheme.typography.titleLarge,
                        color = priceTextColor // Use the transient color state
                    )

                    Text(
                        text = String.format(
                            Locale.US, // FIX: Explicitly use Locale.US
                            "%.2f (%.2f%%)",
                            stockItem.lastChange,
                            (stockItem.lastChange / (stockItem.currentPrice - stockItem.lastChange)) * 100
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = priceTextColor
                    )
                }
            }
        }
    }
}