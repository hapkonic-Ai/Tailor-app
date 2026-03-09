package com.hapkonic.tailorapp.domain.repository

import com.hapkonic.tailorapp.domain.model.Order
import com.hapkonic.tailorapp.domain.model.OrderStatus
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getByStatus(status: OrderStatus, limit: Long = 20, offset: Long = 0): Flow<List<Order>>
    fun getByCustomer(customerId: String): Flow<List<Order>>
    fun getByTailor(tailorId: String): Flow<List<Order>>
    fun getDueToday(startOfDay: Long, endOfDay: Long): Flow<List<Order>>
    fun getById(id: String): Flow<Order?>
    fun getCountByStatus(): Flow<Map<OrderStatus, Long>>
    fun getOrdersInDateRange(startDate: Long, endDate: Long): Flow<List<Order>>
    suspend fun save(order: Order)
    suspend fun updateStatus(orderId: String, status: OrderStatus, updatedAt: Long)
    suspend fun delete(orderId: String)
}
