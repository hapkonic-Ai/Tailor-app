package com.hapkonic.tailorapp.domain.usecase

import com.hapkonic.tailorapp.domain.model.Customer
import com.hapkonic.tailorapp.domain.usecase.customer.SaveCustomerUseCase
import com.hapkonic.tailorapp.fakes.FakeCustomerRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SaveCustomerUseCaseTest {

    private val repo = FakeCustomerRepository()
    private val useCase = SaveCustomerUseCase(repo)

    private fun customer(id: String = "c1", name: String = "Alice") = Customer(
        id            = id,
        name          = name,
        phone         = "9999999999",
        address       = "123 Main St",
        createdAt     = 1_000L,
        lastOrderDate = null,
        updatedAt     = 1_000L
    )

    @Test
    fun `save delegates to repository`() = runTest {
        val c = customer()
        useCase(c)
        assertTrue(repo.savedCustomers.contains(c))
    }

    @Test
    fun `save passes exact customer object`() = runTest {
        val c = customer(id = "abc-123", name = "Bob")
        useCase(c)
        assertEquals(c, repo.savedCustomers.last())
    }

    @Test
    fun `multiple customers can be saved`() = runTest {
        useCase(customer("c1", "Alice"))
        useCase(customer("c2", "Bob"))
        assertEquals(2, repo.savedCustomers.size)
    }
}
