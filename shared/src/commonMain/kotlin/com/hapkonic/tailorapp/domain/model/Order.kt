package com.hapkonic.tailorapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Order(
    val id: String,
    val customerId: String,
    val orderDate: Long,
    val deliveryDate: Long,
    val status: OrderStatus,
    val assignedTailorId: String,
    val price: Double,
    val clothImageUrl: String?,
    val designImageUrl: String?,
    val notes: String?,
    val updatedAt: Long
) : HasTimestamp
