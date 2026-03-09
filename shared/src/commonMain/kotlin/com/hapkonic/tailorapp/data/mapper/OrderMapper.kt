package com.hapkonic.tailorapp.data.mapper

import com.hapkonic.tailorapp.data.remote.dto.OrderDto
import com.hapkonic.tailorapp.db.Orders
import com.hapkonic.tailorapp.domain.model.Order
import com.hapkonic.tailorapp.domain.model.OrderStatus

fun Orders.toDomain() = Order(
    id               = id,
    customerId       = customerId,
    orderDate        = orderDate,
    deliveryDate     = deliveryDate,
    status           = OrderStatus.valueOf(status),
    assignedTailorId = assignedTailorId,
    price            = price,
    clothImageUrl    = clothImageUrl,
    designImageUrl   = designImageUrl,
    notes            = notes,
    updatedAt        = updatedAt
)

fun Order.toEntity() = Orders(
    id               = id,
    customerId       = customerId,
    orderDate        = orderDate,
    deliveryDate     = deliveryDate,
    status           = status.name,
    assignedTailorId = assignedTailorId,
    price            = price,
    clothImageUrl    = clothImageUrl,
    designImageUrl   = designImageUrl,
    notes            = notes,
    updatedAt        = updatedAt
)

fun OrderDto.toDomain() = Order(
    id               = id,
    customerId       = customerId,
    orderDate        = orderDate,
    deliveryDate     = deliveryDate,
    status           = OrderStatus.valueOf(status),
    assignedTailorId = assignedTailorId,
    price            = price,
    clothImageUrl    = clothImageUrl,
    designImageUrl   = designImageUrl,
    notes            = notes,
    updatedAt        = updatedAt
)

fun Order.toDto() = OrderDto(
    id               = id,
    customerId       = customerId,
    orderDate        = orderDate,
    deliveryDate     = deliveryDate,
    status           = status.name,
    assignedTailorId = assignedTailorId,
    price            = price,
    clothImageUrl    = clothImageUrl,
    designImageUrl   = designImageUrl,
    notes            = notes,
    updatedAt        = updatedAt
)
