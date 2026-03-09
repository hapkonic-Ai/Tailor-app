package com.hapkonic.tailorapp.utils

import com.hapkonic.tailorapp.domain.model.Order

/** Builds a CSV string from a list of orders. */
fun buildOrderCsv(orders: List<Order>): String {
    val header = "Order ID,Customer ID,Tailor ID,Status,Price,Order Date,Delivery Date,Notes"
    val rows = orders.joinToString("\n") { o ->
        listOf(
            o.id,
            o.customerId,
            o.assignedTailorId ?: "",
            o.status.name,
            o.price.toString(),
            o.orderDate.toString(),
            o.deliveryDate.toString(),
            (o.notes ?: "").replace(",", ";")
        ).joinToString(",")
    }
    return "$header\n$rows"
}

/**
 * Saves [content] as a CSV file named [filename] to the platform's documents/downloads
 * directory. Returns the absolute file path on success.
 */
expect suspend fun saveCsvFile(filename: String, content: String): String
