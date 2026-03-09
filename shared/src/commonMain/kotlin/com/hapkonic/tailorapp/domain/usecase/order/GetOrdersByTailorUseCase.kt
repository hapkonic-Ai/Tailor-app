package com.hapkonic.tailorapp.domain.usecase.order

import com.hapkonic.tailorapp.domain.model.Order
import com.hapkonic.tailorapp.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow

class GetOrdersByTailorUseCase(private val repo: OrderRepository) {
    operator fun invoke(tailorId: String): Flow<List<Order>> = repo.getByTailor(tailorId)
}
