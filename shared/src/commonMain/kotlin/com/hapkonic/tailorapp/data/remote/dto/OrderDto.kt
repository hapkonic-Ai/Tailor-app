package com.hapkonic.tailorapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class OrderDto(
    val id: String = "",
    val customerId: String = "",
    val orderDate: Long = 0L,
    val deliveryDate: Long = 0L,
    val status: String = "PENDING",
    val assignedTailorId: String = "",
    val price: Double = 0.0,
    val clothImageUrl: String? = null,
    val designImageUrl: String? = null,
    val notes: String? = null,
    val updatedAt: Long = 0L
)
