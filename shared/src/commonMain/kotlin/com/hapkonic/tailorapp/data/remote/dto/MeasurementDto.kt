package com.hapkonic.tailorapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MeasurementDto(
    val id: String = "",
    val customerId: String = "",
    val shoulder: Double = 0.0,
    val chest: Double = 0.0,
    val waist: Double = 0.0,
    val hip: Double = 0.0,
    val sleeveLength: Double = 0.0,
    val shirtLength: Double = 0.0,
    val pantLength: Double = 0.0,
    val notes: String? = null,
    val updatedAt: Long = 0L
)
