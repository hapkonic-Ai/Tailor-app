package com.hapkonic.tailorapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CustomerDto(
    val id: String = "",
    val name: String = "",
    val phone: String = "",
    val address: String = "",
    val createdAt: Long = 0L,
    val lastOrderDate: Long? = null,
    val updatedAt: Long = 0L
)
