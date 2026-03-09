package com.hapkonic.tailorapp.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.hapkonic.tailorapp.data.mapper.toDomain
import com.hapkonic.tailorapp.data.sync.SyncQueue
import com.hapkonic.tailorapp.data.sync.currentTimeMillis
import com.hapkonic.tailorapp.db.AppDatabase
import com.hapkonic.tailorapp.domain.model.Customer
import com.hapkonic.tailorapp.domain.model.SyncAction
import com.hapkonic.tailorapp.domain.repository.CustomerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CustomerRepositoryImpl(
    private val db: AppDatabase,
    private val syncQueue: SyncQueue
) : CustomerRepository {

    override fun getAll(limit: Long, offset: Long): Flow<List<Customer>> =
        db.customersQueries.getAll(limit, offset)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { list -> list.map { it.toDomain() } }

    override fun getById(id: String): Flow<Customer?> =
        db.customersQueries.getById(id)
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .map { it?.toDomain() }

    override fun searchByName(query: String): Flow<List<Customer>> =
        db.customersQueries.searchByName(query)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { list -> list.map { it.toDomain() } }

    override fun searchByPhone(query: String): Flow<List<Customer>> =
        db.customersQueries.searchByPhone(query)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { list -> list.map { it.toDomain() } }

    override fun getCount(): Flow<Long> =
        db.customersQueries.getCount()
            .asFlow()
            .map { q -> q.executeAsOne() }

    override suspend fun save(customer: Customer) {
        db.customersQueries.insert(
            id            = customer.id,
            name          = customer.name,
            phone         = customer.phone,
            address       = customer.address,
            createdAt     = customer.createdAt,
            lastOrderDate = customer.lastOrderDate,
            updatedAt     = customer.updatedAt
        )
        syncQueue.enqueue(customer.id, "customer", SyncAction.CREATE)
    }

    override suspend fun delete(id: String) {
        db.customersQueries.delete(id)
        syncQueue.enqueue(id, "customer", SyncAction.DELETE)
    }
}
