package com.hapkonic.tailorapp.fakes

import com.hapkonic.tailorapp.domain.model.Order
import com.hapkonic.tailorapp.domain.model.OrderStatus
import com.hapkonic.tailorapp.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeOrderRepository : OrderRepository {

    private val orders = MutableStateFlow<List<Order>>(emptyList())

    /** Recorded calls to updateStatus: Pair(orderId, newStatus) */
    val statusUpdates = mutableListOf<Pair<String, OrderStatus>>()

    fun seed(vararg order: Order) { orders.value = order.toList() }

    override fun getByStatus(status: OrderStatus, limit: Long, offset: Long): Flow<List<Order>> =
        orders.map { list -> list.filter { it.status == status } }

    override fun getByCustomer(customerId: String): Flow<List<Order>> =
        orders.map { list -> list.filter { it.customerId == customerId } }

    override fun getByTailor(tailorId: String): Flow<List<Order>> =
        orders.map { list -> list.filter { it.assignedTailorId == tailorId } }

    override fun getDueToday(startOfDay: Long, endOfDay: Long): Flow<List<Order>> =
        orders.map { list -> list.filter { it.deliveryDate in startOfDay..endOfDay } }

    override fun getById(id: String): Flow<Order?> =
        orders.map { list -> list.firstOrNull { it.id == id } }

    override fun getCountByStatus(): Flow<Map<OrderStatus, Long>> =
        orders.map { list ->
            list.groupBy { it.status }.mapValues { (_, v) -> v.size.toLong() }
        }

    override fun getOrdersInDateRange(startDate: Long, endDate: Long): Flow<List<Order>> =
        orders.map { list -> list.filter { it.orderDate in startDate..endDate } }

    override suspend fun save(order: Order) {
        orders.value = orders.value.filterNot { it.id == order.id } + order
    }

    override suspend fun updateStatus(orderId: String, status: OrderStatus, updatedAt: Long) {
        statusUpdates += orderId to status
        orders.value = orders.value.map { o ->
            if (o.id == orderId) o.copy(status = status, updatedAt = updatedAt) else o
        }
    }

    override suspend fun delete(orderId: String) {
        orders.value = orders.value.filterNot { it.id == orderId }
    }
}
