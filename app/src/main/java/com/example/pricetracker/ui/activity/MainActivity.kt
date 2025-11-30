package com.example.pricetracker.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.pricetracker.presentation.navigation.PriceTrackerAppNavHost
import com.example.pricetracker.ui.theme.PriceTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PriceTrackerTheme {
                PriceTrackerAppNavHost()
            }
        }
    }
}