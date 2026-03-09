package com.hapkonic.tailorapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Customer(
    val id: String,
    val name: String,
    val phone: String,
    val address: String,
    val createdAt: Long,
    val lastOrderDate: Long?,
    override val updatedAt: Long
) : HasTimestamp
