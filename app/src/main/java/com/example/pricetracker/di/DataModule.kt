package com.example.pricetracker.di

import com.example.pricetracker.data.PostmanEchoWebSocketClient
import com.example.pricetracker.data.StockRepository
import com.example.pricetracker.data.StockRepositoryImpl
import com.example.pricetracker.data.WebSocketClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

val dataModule = module {

    single<CoroutineScope> { CoroutineScope(SupervisorJob() + Dispatchers.IO) }

    single<WebSocketClient> { PostmanEchoWebSocketClient(scope = get()) }

    single<StockRepository> { StockRepositoryImpl(client = get(), scope = get()) }
}