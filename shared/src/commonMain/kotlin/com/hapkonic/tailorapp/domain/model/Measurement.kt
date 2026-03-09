package com.hapkonic.tailorapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Measurement(
    val id: String,
    val customerId: String,
    val shoulder: Double,
    val chest: Double,
    val waist: Double,
    val hip: Double,
    val sleeveLength: Double,
    val shirtLength: Double,
    val pantLength: Double,
    val notes: String?,
    val updatedAt: Long
) : HasTimestamp
