package com.hapkonic.tailorapp.utils

import com.hapkonic.tailorapp.domain.model.Order
import com.hapkonic.tailorapp.domain.model.OrderStatus
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CsvExporterTest {

    private fun order(
        id: String = "ord-001",
        customerId: String = "cust-1",
        tailorId: String = "tailor-1",
        status: OrderStatus = OrderStatus.PENDING,
        price: Double = 500.0,
        notes: String? = null
    ) = Order(
        id               = id,
        customerId       = customerId,
        orderDate        = 1_700_000_000_000L,
        deliveryDate     = 1_700_100_000_000L,
        status           = status,
        assignedTailorId = tailorId,
        price            = price,
        clothImageUrl    = null,
        designImageUrl   = null,
        notes            = notes,
        updatedAt        = 1_700_000_000_000L
    )

    @Test
    fun `csv has correct header`() {
        val csv = buildOrderCsv(emptyList())
        val header = csv.lines().first()
        assertEquals("Order ID,Customer ID,Tailor ID,Status,Price,Order Date,Delivery Date,Notes", header)
    }

    @Test
    fun `csv has correct number of rows`() {
        val csv = buildOrderCsv(listOf(order("o1"), order("o2")))
        val lines = csv.lines().filter { it.isNotBlank() }
        assertEquals(3, lines.size) // 1 header + 2 data rows
    }

    @Test
    fun `csv row contains order id`() {
        val csv = buildOrderCsv(listOf(order(id = "unique-order-id")))
        assertContains(csv, "unique-order-id")
    }

    @Test
    fun `csv row contains status`() {
        val csv = buildOrderCsv(listOf(order(status = OrderStatus.READY)))
        assertContains(csv, "READY")
    }

    @Test
    fun `commas in notes are replaced with semicolons`() {
        val csv = buildOrderCsv(listOf(order(notes = "hem, sleeve, collar")))
        val dataRow = csv.lines().drop(1).first()
        assertTrue(dataRow.endsWith("hem; sleeve; collar"))
    }

    @Test
    fun `empty notes produce empty field`() {
        val csv = buildOrderCsv(listOf(order(notes = null)))
        val dataRow = csv.lines().drop(1).first()
        assertTrue(dataRow.endsWith(","))
    }

    @Test
    fun `empty order list produces header only`() {
        val csv = buildOrderCsv(emptyList())
        val lines = csv.lines().filter { it.isNotBlank() }
        assertEquals(1, lines.size)
    }
}
