package com.hapkonic.tailorapp.domain.usecase

import app.cash.turbine.test
import com.hapkonic.tailorapp.domain.model.Customer
import com.hapkonic.tailorapp.domain.usecase.customer.GetCustomersUseCase
import com.hapkonic.tailorapp.domain.usecase.customer.SearchCustomersUseCase
import com.hapkonic.tailorapp.fakes.FakeCustomerRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetCustomersUseCaseTest {

    private val repo = FakeCustomerRepository()
    private val getCustomers = GetCustomersUseCase(repo)
    private val searchCustomers = SearchCustomersUseCase(repo)

    private fun customer(id: String, name: String, phone: String = "0000") = Customer(
        id            = id,
        name          = name,
        phone         = phone,
        address       = "",
        createdAt     = 1_000L,
        lastOrderDate = null,
        updatedAt     = 1_000L
    )

    @Test
    fun `getAll emits seeded customers`() = runTest {
        repo.seed(
            customer("c1", "Alice"),
            customer("c2", "Bob")
        )
        getCustomers().test {
            val items = awaitItem()
            assertEquals(2, items.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getAll emits empty list when no customers`() = runTest {
        getCustomers().test {
            assertTrue(awaitItem().isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `searchCustomers by name returns matching customers`() = runTest {
        repo.seed(
            customer("c1", "Alice"),
            customer("c2", "Bob"),
            customer("c3", "Alicia")
        )
        searchCustomers("ali").test {
            val items = awaitItem()
            assertEquals(2, items.size)
            assertTrue(items.all { it.name.contains("ali", ignoreCase = true) })
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `searchCustomers by phone returns matching customers`() = runTest {
        repo.seed(
            customer("c1", "Alice", phone = "9876543210"),
            customer("c2", "Bob",   phone = "1234567890")
        )
        searchCustomers("987").test {
            val items = awaitItem()
            assertEquals(1, items.size)
            assertEquals("Alice", items.first().name)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
