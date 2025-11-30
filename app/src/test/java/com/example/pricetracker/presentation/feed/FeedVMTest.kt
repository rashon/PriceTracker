package com.example.pricetracker.presentation.feed

import app.cash.turbine.test
import com.example.pricetracker.data.StockRepository
import com.example.pricetracker.domain.model.StockItem
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FeedVMTest {

    private lateinit var viewModel: FeedVM
    private lateinit var repository: StockRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is loading`() = runTest {
        every { repository.stockFeed } returns flowOf(emptyList())
        every { repository.connectionStatus } returns MutableStateFlow(false)

        viewModel = FeedVM(repository)

        assertEquals(true, viewModel.state.value.isLoading)
    }

    @Test
    fun `state updates when repository emits stocks`() = runTest {
        val stocks = listOf(
            StockItem("AAPL", "Apple", 150.0, 1.0, "Tech"),
            StockItem("GOOGL", "Google", 2800.0, -5.0, "Tech")
        )
        every { repository.stockFeed } returns flowOf(stocks)
        every { repository.connectionStatus } returns MutableStateFlow(true)

        viewModel = FeedVM(repository)

        viewModel.state.test {
            // Initial loading
            val item1 = awaitItem()
            assertTrue(item1.isLoading)

            // Mapped state
            val item2 = awaitItem()
            assertFalse(item2.isLoading)
            assertEquals(stocks, item2.stocks)
            assertTrue(item2.isConnected)
        }
    }

    @Test
    fun `toggleFeed connects and disconnects repository`() {
        every { repository.stockFeed } returns flowOf(emptyList())
        every { repository.connectionStatus } returns MutableStateFlow(false)
        viewModel = FeedVM(repository)

        // Initial state is true (enabled)
        assertTrue(viewModel.isLiveUpdateEnabled.value)

        // Toggle off
        viewModel.toggleFeed(false)
        assertFalse(viewModel.isLiveUpdateEnabled.value)
        verify { repository.disconnectFeed() }

        // Toggle on
        viewModel.toggleFeed(true)
        assertTrue(viewModel.isLiveUpdateEnabled.value)
        verify { repository.connectFeed() }
    }
}