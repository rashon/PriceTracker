package com.example.pricetracker.data

import com.example.pricetracker.domain.model.StockItem
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import kotlin.random.Random

private const val WEBSOCKET_URL = "wss://ws.postman-echo.com/raw"
private const val UPDATE_INTERVAL_MS = 2000L

class PostmanEchoWebSocketClient(
    private val scope: CoroutineScope
) : WebSocketClient {

    private val _stockUpdates = MutableSharedFlow<List<StockItem>>(replay = 1)
    override val stockUpdates: SharedFlow<List<StockItem>> = _stockUpdates

    private var currentStocks: List<StockItem> = emptyList()
    private val stocksMutex = Mutex() // Mutex to ensure thread-safe updates to currentStocks

    private var connectionJob: Job? = null
    private var sendJob: Job? = null
    private var receiveJob: Job? = null

    var session: WebSocketSession? = null

    private val client = HttpClient(Android) {

        install(WebSockets)

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
    }

    override fun connect(initialStocks: List<StockItem>) {

        if (connectionJob?.isActive == true) return

        this.currentStocks = initialStocks

        connectionJob = scope.launch {
            try {

                if (session == null) {

                    session = client.webSocketSession(
                        method = HttpMethod.Get,
                        host = "ws.postman-echo.com",
                        path = "/raw"
                    )

                    println("Connected to $WEBSOCKET_URL successfully. Starting send/receive jobs.")
                }

                session?.let {
                    receiveJob = listenForWSMessages(it)
                    sendJob = sendUpdatesToWSOnInterval(it)
                }

                sendJob?.join()
                receiveJob?.join()

            } catch (e: Exception) {

                println("WebSocket Connection Error: ${e.message}")

                disconnect()
            } finally {

                session?.close()
                sendJob?.cancel()
                receiveJob?.cancel()
            }
        }
    }

    private fun listenForWSMessages(session: WebSocketSession): Job = scope.launch {
        try {

            for (frame in session.incoming) {

                if (frame is Frame.Text) {

                    val receivedMessage = frame.readText()

                    val receivedDto = Json.decodeFromString<PriceUpdateDto>(receivedMessage)

                    stocksMutex.withLock {
                        val oldStock = currentStocks.find { it.symbol == receivedDto.symbol }

                        if (oldStock != null) {

                            val updatedStock = oldStock.copy(
                                currentPrice = receivedDto.price,
                                lastChange = receivedDto.price - oldStock.currentPrice,
                                timestamp = System.currentTimeMillis()
                            )

                            currentStocks = currentStocks.map {
                                if (it.symbol == updatedStock.symbol) updatedStock else it
                            }

                            _stockUpdates.emit(currentStocks)
                        }
                    }
                }
            }
        } catch (e: Exception) {

            println("WebSocket Receiving Error: ${e.message}")
        }
    }

    private fun sendUpdatesToWSOnInterval(
        session: WebSocketSession,
        interval: Long = UPDATE_INTERVAL_MS
    ): Job = scope.launch {

        try {

            while (isActive) {

                delay(interval)

                val stocksToSend = stocksMutex.withLock { currentStocks.toList() }

                for (stock in stocksToSend) {

                    val tempStock = generateNewPrice(stock)

                    val updateDto =
                        PriceUpdateDto(symbol = tempStock.symbol, price = tempStock.currentPrice)

                    val jsonMessage = Json.encodeToString(updateDto)

                    session.send(Frame.Text(jsonMessage))

                    println("WebSocket Sent: $jsonMessage")
                }
            }
        } catch (e: Exception) {

            println("WebSocket Sending Error: ${e.message}")
        }
    }

    override fun disconnect() {
        // Cancel all associated jobs and close the client
        connectionJob?.cancel()
        sendJob?.cancel()
        receiveJob?.cancel()
        client.close()
        println("Disconnected from $WEBSOCKET_URL and client closed.")
    }

    /**
     * Generates a new random price for a given stock.
     */
    private fun generateNewPrice(oldStock: StockItem): StockItem {
        val volatility = oldStock.currentPrice * 0.01 // 1% max movement
        val change = Random.nextDouble(-volatility, volatility)
        val newPrice = (oldStock.currentPrice + change).coerceAtLeast(0.01)

        return oldStock.copy(
            currentPrice = String.format("%.2f", newPrice).toDouble(),
            lastChange = String.format("%.2f", change).toDouble(),
            timestamp = System.currentTimeMillis()
        )
    }
}