package com.hapkonic.tailorapp.data.mapper

import com.hapkonic.tailorapp.data.remote.dto.CustomerDto
import com.hapkonic.tailorapp.db.Customers
import com.hapkonic.tailorapp.domain.model.Customer

// ── SQLDelight entity → Domain ────────────────────────────────────────────────
fun Customers.toDomain() = Customer(
    id            = id,
    name          = name,
    phone         = phone,
    address       = address,
    createdAt     = createdAt,
    lastOrderDate = lastOrderDate,
    updatedAt     = updatedAt
)

// ── Domain → SQLDelight entity ────────────────────────────────────────────────
fun Customer.toEntity() = Customers(
    id            = id,
    name          = name,
    phone         = phone,
    address       = address,
    createdAt     = createdAt,
    lastOrderDate = lastOrderDate,
    updatedAt     = updatedAt
)

// ── DTO → Domain ──────────────────────────────────────────────────────────────
fun CustomerDto.toDomain() = Customer(
    id            = id,
    name          = name,
    phone         = phone,
    address       = address,
    createdAt     = createdAt,
    lastOrderDate = lastOrderDate,
    updatedAt     = updatedAt
)

// ── Domain → DTO ──────────────────────────────────────────────────────────────
fun Customer.toDto() = CustomerDto(
    id            = id,
    name          = name,
    phone         = phone,
    address       = address,
    createdAt     = createdAt,
    lastOrderDate = lastOrderDate,
    updatedAt     = updatedAt
)
