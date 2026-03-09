package com.hapkonic.tailorapp.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.hapkonic.tailorapp.data.mapper.toDomain
import com.hapkonic.tailorapp.data.sync.SyncQueue
import com.hapkonic.tailorapp.db.AppDatabase
import com.hapkonic.tailorapp.domain.model.SyncAction
import com.hapkonic.tailorapp.domain.model.Tailor
import com.hapkonic.tailorapp.domain.repository.TailorRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TailorRepositoryImpl(
    private val db: AppDatabase,
    private val syncQueue: SyncQueue
) : TailorRepository {

    override fun getAll(): Flow<List<Tailor>> =
        db.tailorsQueries.getAll()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { list -> list.map { it.toDomain() } }

    override fun getById(id: String): Flow<Tailor?> =
        db.tailorsQueries.getById(id)
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .map { it?.toDomain() }

    override suspend fun save(tailor: Tailor) {
        db.tailorsQueries.insert(
            id             = tailor.id,
            name           = tailor.name,
            phone          = tailor.phone,
            specialization = tailor.specialization,
            activeOrders   = tailor.activeOrders.toLong(),
            updatedAt      = tailor.updatedAt
        )
        syncQueue.enqueue(tailor.id, "tailor", SyncAction.CREATE)
    }

    override suspend fun delete(id: String) {
        db.tailorsQueries.delete(id)
        syncQueue.enqueue(id, "tailor", SyncAction.DELETE)
    }
}
