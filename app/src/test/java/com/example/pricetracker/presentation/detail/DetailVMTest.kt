package com.example.pricetracker.presentation.detail

import app.cash.turbine.test
import com.example.pricetracker.data.StockRepository
import com.example.pricetracker.domain.model.StockItem
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailVMTest {

    private lateinit var viewModel: DetailVM
    private lateinit var repository: StockRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `state is initially loading`() = runTest {
        every { repository.getStockBySymbol("AAPL") } returns flowOf()
        viewModel = DetailVM(repository, "AAPL")

        assertEquals(true, viewModel.detailsState.value.isLoading)
    }

    @Test
    fun `when repository returns stock, state is updated`() = runTest {
        val stock = StockItem(
            symbol = "AAPL",
            name = "Apple",
            currentPrice = 150.0,
            lastChange = 2.0,
            description = "Tech",
            timestamp = 12345L
        )
        every { repository.getStockBySymbol("AAPL") } returns flowOf(stock)

        viewModel = DetailVM(repository, "AAPL")

        viewModel.detailsState.test {
            // First emission is initial value (isLoading=true)
            val item1 = awaitItem()
            assertTrue(item1.isLoading)

            // Second emission is the mapped value
            val item2 = awaitItem()
            assertFalse(item2.isLoading)
            assertEquals(stock, item2.stock)
            assertNull(item2.error)
        }
    }

    @Test
    fun `when repository returns null, state shows error`() = runTest {
        every { repository.getStockBySymbol("AAPL") } returns flowOf(null)

        viewModel = DetailVM(repository, "AAPL")

        viewModel.detailsState.test {
            val item1 = awaitItem()
            assertTrue(item1.isLoading)

            val item2 = awaitItem()
            assertFalse(item2.isLoading)
            assertNull(item2.stock)
            assertEquals("Stock details not found for AAPL.", item2.error)
        }
    }
}