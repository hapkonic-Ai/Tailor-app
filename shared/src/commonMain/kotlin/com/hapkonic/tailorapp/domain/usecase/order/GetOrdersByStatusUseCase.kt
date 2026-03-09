package com.hapkonic.tailorapp.domain.usecase.order

import com.hapkonic.tailorapp.domain.model.Order
import com.hapkonic.tailorapp.domain.model.OrderStatus
import com.hapkonic.tailorapp.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow

class GetOrdersByStatusUseCase(private val repo: OrderRepository) {
    operator fun invoke(status: OrderStatus, limit: Long = 20, offset: Long = 0): Flow<List<Order>> =
        repo.getByStatus(status, limit, offset)
}
