package com.hapkonic.tailorapp.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class OrderStatusTest {

    @Test
    fun `PENDING next is IN_PROGRESS`() {
        assertEquals(OrderStatus.IN_PROGRESS, OrderStatus.PENDING.next())
    }

    @Test
    fun `IN_PROGRESS next is READY`() {
        assertEquals(OrderStatus.READY, OrderStatus.IN_PROGRESS.next())
    }

    @Test
    fun `READY next is DELIVERED`() {
        assertEquals(OrderStatus.DELIVERED, OrderStatus.READY.next())
    }

    @Test
    fun `DELIVERED next is null`() {
        assertNull(OrderStatus.DELIVERED.next())
    }

    @Test
    fun `all statuses are covered`() {
        assertEquals(4, OrderStatus.entries.size)
    }
}
