package com.hapkonic.tailorapp.domain.usecase.order

import com.hapkonic.tailorapp.domain.model.Order
import com.hapkonic.tailorapp.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow

class GetOrderByIdUseCase(private val repo: OrderRepository) {
    operator fun invoke(id: String): Flow<Order?> = repo.getById(id)
}
