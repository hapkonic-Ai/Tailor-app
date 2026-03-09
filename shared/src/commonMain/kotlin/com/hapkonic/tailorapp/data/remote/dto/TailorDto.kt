package com.hapkonic.tailorapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TailorDto(
    val id: String = "",
    val name: String = "",
    val phone: String = "",
    val specialization: String = "",
    val activeOrders: Int = 0,
    val updatedAt: Long = 0L
)
