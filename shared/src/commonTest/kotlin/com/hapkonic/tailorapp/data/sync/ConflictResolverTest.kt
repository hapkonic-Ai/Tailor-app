package com.hapkonic.tailorapp.data.sync

import com.hapkonic.tailorapp.domain.model.Customer
import kotlin.test.Test
import kotlin.test.assertEquals

class ConflictResolverTest {

    private val resolver = ConflictResolver()

    private fun customer(updatedAt: Long) = Customer(
        id            = "c1",
        name          = "Test",
        phone         = "0000",
        address       = "",
        createdAt     = 1_000L,
        lastOrderDate = null,
        updatedAt     = updatedAt
    )

    @Test
    fun `local newer — local wins`() {
        val local  = customer(updatedAt = 200L)
        val remote = customer(updatedAt = 100L)
        assertEquals(local, resolver.resolve(local, remote))
    }

    @Test
    fun `remote newer — remote wins`() {
        val local  = customer(updatedAt = 100L)
        val remote = customer(updatedAt = 200L)
        assertEquals(remote, resolver.resolve(local, remote))
    }

    @Test
    fun `same timestamp — remote wins`() {
        val local  = customer(updatedAt = 100L)
        val remote = customer(updatedAt = 100L)
        assertEquals(remote, resolver.resolve(local, remote))
    }
}
