package com.example.pricetracker.data

import com.example.pricetracker.domain.model.StockItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PostmanWebSocketClientTest {

    private lateinit var client: PostmanEchoWebSocketClient
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private val scope = CoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        client = PostmanEchoWebSocketClient(scope)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        client.disconnect()
    }

    @Test
    fun `client initialization creates flow`() {
        assertNotNull(client.stockUpdates)
    }

    @Test
    fun `connect starts session`() = testScope.runTest {
        val initialStocks = listOf(
            StockItem("AAPL", "Apple", 150.0, 0.0, "Tech")
        )

        client.connect(initialStocks)
        client.disconnect()
    }
}