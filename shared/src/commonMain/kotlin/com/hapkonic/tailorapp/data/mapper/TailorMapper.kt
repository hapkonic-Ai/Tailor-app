package com.hapkonic.tailorapp.data.mapper

import com.hapkonic.tailorapp.data.remote.dto.TailorDto
import com.hapkonic.tailorapp.db.Tailors
import com.hapkonic.tailorapp.domain.model.Tailor

fun Tailors.toDomain() = Tailor(
    id             = id,
    name           = name,
    phone          = phone,
    specialization = specialization,
    activeOrders   = activeOrders.toInt(),
    updatedAt      = updatedAt
)

fun Tailor.toEntity() = Tailors(
    id             = id,
    name           = name,
    phone          = phone,
    specialization = specialization,
    activeOrders   = activeOrders.toLong(),
    updatedAt      = updatedAt
)

fun TailorDto.toDomain() = Tailor(
    id             = id,
    name           = name,
    phone          = phone,
    specialization = specialization,
    activeOrders   = activeOrders,
    updatedAt      = updatedAt
)

fun Tailor.toDto() = TailorDto(
    id             = id,
    name           = name,
    phone          = phone,
    specialization = specialization,
    activeOrders   = activeOrders,
    updatedAt      = updatedAt
)
