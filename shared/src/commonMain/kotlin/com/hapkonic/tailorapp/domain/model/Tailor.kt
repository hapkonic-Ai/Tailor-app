package com.hapkonic.tailorapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Tailor(
    val id: String,
    val name: String,
    val phone: String,
    val specialization: String,
    val activeOrders: Int,
    val updatedAt: Long
) : HasTimestamp
