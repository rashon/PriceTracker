package com.example.pricetracker.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pricetracker.presentation.detail.DetailVM
import com.example.pricetracker.ui.theme.greenIndicator
import com.example.pricetracker.ui.theme.redIndicator
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@Composable
fun DetailScreen(
    viewModel: DetailVM
) {

    val stockDetailsState by viewModel.detailsState.collectAsState()

    val decimalFormatUS = DecimalFormatSymbols(Locale.US)
    val priceFormat = DecimalFormat("#,##0.00", decimalFormatUS)
    val changeFormat = DecimalFormat("0.00", decimalFormatUS)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        when {
            stockDetailsState.isLoading -> Text(
                "Loading stock details...",
                style = MaterialTheme.typography.titleMedium
            )

            stockDetailsState.error != null -> Text(
                "Error: ${stockDetailsState.error}",
                color = MaterialTheme.colorScheme.error
            )

            stockDetailsState.stock != null -> {

                stockDetailsState.stock?.let { stock ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {

                        Column(modifier = Modifier.padding(16.dp)) {

                            Text(
                                text = "Current Price:",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = "$${priceFormat.format(stock.currentPrice)}",
                                style = MaterialTheme.typography.headlineLarge,
                                fontSize = 48.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )

                            val changeColor = when {
                                stock.lastChange > 0 -> greenIndicator
                                stock.lastChange < 0 -> redIndicator
                                else -> Color.Gray
                            }

                            val changeSign = if (stock.lastChange > 0) "+" else ""

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Text(
                                    text = "$changeSign${changeFormat.format(stock.lastChange)}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = changeColor
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = stock.name,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stock.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                    ?: Text("No stock details available.")
            }
        }
    }
}