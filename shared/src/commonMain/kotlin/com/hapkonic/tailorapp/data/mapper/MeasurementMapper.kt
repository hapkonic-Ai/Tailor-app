package com.hapkonic.tailorapp.data.mapper

import com.hapkonic.tailorapp.data.remote.dto.MeasurementDto
import com.hapkonic.tailorapp.db.Measurements
import com.hapkonic.tailorapp.domain.model.Measurement

fun Measurements.toDomain() = Measurement(
    id           = id,
    customerId   = customerId,
    shoulder     = shoulder,
    chest        = chest,
    waist        = waist,
    hip          = hip,
    sleeveLength = sleeveLength,
    shirtLength  = shirtLength,
    pantLength   = pantLength,
    notes        = notes,
    updatedAt    = updatedAt
)

fun Measurement.toEntity() = Measurements(
    id           = id,
    customerId   = customerId,
    shoulder     = shoulder,
    chest        = chest,
    waist        = waist,
    hip          = hip,
    sleeveLength = sleeveLength,
    shirtLength  = shirtLength,
    pantLength   = pantLength,
    notes        = notes,
    updatedAt    = updatedAt
)

fun MeasurementDto.toDomain() = Measurement(
    id           = id,
    customerId   = customerId,
    shoulder     = shoulder,
    chest        = chest,
    waist        = waist,
    hip          = hip,
    sleeveLength = sleeveLength,
    shirtLength  = shirtLength,
    pantLength   = pantLength,
    notes        = notes,
    updatedAt    = updatedAt
)

fun Measurement.toDto() = MeasurementDto(
    id           = id,
    customerId   = customerId,
    shoulder     = shoulder,
    chest        = chest,
    waist        = waist,
    hip          = hip,
    sleeveLength = sleeveLength,
    shirtLength  = shirtLength,
    pantLength   = pantLength,
    notes        = notes,
    updatedAt    = updatedAt
)
