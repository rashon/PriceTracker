package com.example.pricetracker.data

import app.cash.turbine.test
import com.example.pricetracker.domain.model.StockItem
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StockRepositoryImplTest {

    private lateinit var repository: StockRepositoryImpl
    private lateinit var client: WebSocketClient
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        client = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init starts feed and connects client`() = testScope.runTest {
        val stockUpdatesFlow = MutableSharedFlow<List<StockItem>>()
        every { client.stockUpdates } returns stockUpdatesFlow

        repository = StockRepositoryImpl(client, this)
        advanceUntilIdle()

        verify { client.connect(any()) }

        // Initially false until we receive an update
        assertFalse(repository.connectionStatus.value)

        // Emit an update
        stockUpdatesFlow.emit(emptyList())
        advanceUntilIdle()

        // Should be true now
        assertTrue(repository.connectionStatus.value)
    }

    @Test
    fun `stockFeed emits sorted stocks`() = testScope.runTest {
        val stockUpdatesFlow = MutableSharedFlow<List<StockItem>>()
        every { client.stockUpdates } returns stockUpdatesFlow

        repository = StockRepositoryImpl(client, this)

        val stock1 = StockItem("AAPL", "Apple", 100.0, 0.0, "Tech")
        val stock2 = StockItem("GOOGL", "Google", 200.0, 0.0, "Tech")

        repository.stockFeed.test {
            // First emission is the initial state (from constants)
            awaitItem()

            // Trigger update
            stockUpdatesFlow.emit(listOf(stock1, stock2))

            val items = awaitItem()
            assertEquals(2, items.size)
            // Higher price first (GOOGL: 200.0 > AAPL: 100.0)
            assertEquals("GOOGL", items[0].symbol)
            assertEquals("AAPL", items[1].symbol)
        }
    }

    @Test
    fun `connectFeed connects client`() = testScope.runTest {
        val stockUpdatesFlow = MutableSharedFlow<List<StockItem>>()
        every { client.stockUpdates } returns stockUpdatesFlow
        repository = StockRepositoryImpl(client, this)

        // Initially connected in init
        verify(exactly = 1) { client.connect(any()) }

        // Disconnect
        repository.disconnectFeed()
        verify { client.disconnect() }
        assertFalse(repository.connectionStatus.value)

        // Connect again
        repository.connectFeed()
        verify(exactly = 2) { client.connect(any()) }
        assertTrue(repository.connectionStatus.value)
    }

    @Test
    fun `getStockBySymbol returns correct stock`() = testScope.runTest {
        val stockUpdatesFlow = MutableSharedFlow<List<StockItem>>()
        every { client.stockUpdates } returns stockUpdatesFlow
        repository = StockRepositoryImpl(client, this)

        val stock = StockItem("TSLA", "Tesla", 800.0, 0.0, "Auto")

        repository.getStockBySymbol("TSLA").test {
            // Skip initial state
            skipItems(1)

            // Emit new data
            stockUpdatesFlow.emit(listOf(stock))

            val item = awaitItem()
            assertNotNull(item)
            assertEquals("TSLA", item?.symbol)
        }
    }
}
