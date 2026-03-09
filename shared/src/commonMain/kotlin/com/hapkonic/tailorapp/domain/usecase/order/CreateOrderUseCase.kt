package com.hapkonic.tailorapp.domain.usecase.order

import com.hapkonic.tailorapp.domain.model.Order
import com.hapkonic.tailorapp.domain.repository.OrderRepository

class CreateOrderUseCase(private val repo: OrderRepository) {
    suspend operator fun invoke(order: Order) = repo.save(order)
}
