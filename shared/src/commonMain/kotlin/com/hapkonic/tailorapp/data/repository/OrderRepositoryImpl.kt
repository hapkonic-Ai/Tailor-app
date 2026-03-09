package com.hapkonic.tailorapp.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.hapkonic.tailorapp.data.mapper.toDomain
import com.hapkonic.tailorapp.data.sync.SyncQueue
import com.hapkonic.tailorapp.data.sync.currentTimeMillis
import com.hapkonic.tailorapp.db.AppDatabase
import com.hapkonic.tailorapp.domain.model.Order
import com.hapkonic.tailorapp.domain.model.OrderStatus
import com.hapkonic.tailorapp.domain.model.SyncAction
import com.hapkonic.tailorapp.domain.repository.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OrderRepositoryImpl(
    private val db: AppDatabase,
    private val syncQueue: SyncQueue
) : OrderRepository {

    override fun getByStatus(status: OrderStatus, limit: Long, offset: Long): Flow<List<Order>> =
        db.ordersQueries.getByStatus(status.name, limit, offset)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { list -> list.map { it.toDomain() } }

    override fun getByCustomer(customerId: String): Flow<List<Order>> =
        db.ordersQueries.getByCustomer(customerId)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { list -> list.map { it.toDomain() } }

    override fun getByTailor(tailorId: String): Flow<List<Order>> =
        db.ordersQueries.getByTailor(tailorId)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { list -> list.map { it.toDomain() } }

    override fun getDueToday(startOfDay: Long, endOfDay: Long): Flow<List<Order>> =
        db.ordersQueries.getDueToday(startOfDay, endOfDay)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { list -> list.map { it.toDomain() } }

    override fun getById(id: String): Flow<Order?> =
        db.ordersQueries.getById(id)
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .map { it?.toDomain() }

    override fun getCountByStatus(): Flow<Map<OrderStatus, Long>> =
        db.ordersQueries.countByStatus()
            .asFlow()
            .map { query ->
                query.executeAsList().associate { row ->
                    OrderStatus.valueOf(row.status) to row.count
                }
            }

    override fun getOrdersInDateRange(startDate: Long, endDate: Long): Flow<List<Order>> =
        db.ordersQueries.getInDateRange(startDate, endDate)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { list -> list.map { it.toDomain() } }

    override suspend fun save(order: Order) {
        db.ordersQueries.insert(
            id               = order.id,
            customerId       = order.customerId,
            orderDate        = order.orderDate,
            deliveryDate     = order.deliveryDate,
            status           = order.status.name,
            assignedTailorId = order.assignedTailorId,
            price            = order.price,
            clothImageUrl    = order.clothImageUrl,
            designImageUrl   = order.designImageUrl,
            notes            = order.notes,
            updatedAt        = order.updatedAt
        )
        // Update customer's lastOrderDate
        db.customersQueries.updateLastOrderDate(
            lastOrderDate = order.orderDate,
            updatedAt     = currentTimeMillis(),
            id            = order.customerId
        )
        syncQueue.enqueue(order.id, "order", SyncAction.CREATE)
    }

    override suspend fun updateStatus(orderId: String, status: OrderStatus, updatedAt: Long) {
        db.ordersQueries.updateStatus(
            status    = status.name,
            updatedAt = updatedAt,
            id        = orderId
        )
        syncQueue.enqueue(orderId, "order", SyncAction.UPDATE)
    }

    override suspend fun delete(orderId: String) {
        db.ordersQueries.delete(orderId)
        syncQueue.enqueue(orderId, "order", SyncAction.DELETE)
    }
}
