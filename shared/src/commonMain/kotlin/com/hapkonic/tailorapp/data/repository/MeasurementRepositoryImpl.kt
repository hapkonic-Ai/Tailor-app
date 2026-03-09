package com.hapkonic.tailorapp.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.hapkonic.tailorapp.data.mapper.toDomain
import com.hapkonic.tailorapp.data.sync.SyncQueue
import com.hapkonic.tailorapp.db.AppDatabase
import com.hapkonic.tailorapp.domain.model.Measurement
import com.hapkonic.tailorapp.domain.model.SyncAction
import com.hapkonic.tailorapp.domain.repository.MeasurementRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MeasurementRepositoryImpl(
    private val db: AppDatabase,
    private val syncQueue: SyncQueue
) : MeasurementRepository {

    override fun getByCustomer(customerId: String): Flow<List<Measurement>> =
        db.measurementsQueries.getByCustomer(customerId)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { list -> list.map { it.toDomain() } }

    override fun getById(id: String): Flow<Measurement?> =
        db.measurementsQueries.getById(id)
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
            .map { it?.toDomain() }

    override suspend fun save(measurement: Measurement) {
        db.measurementsQueries.insert(
            id           = measurement.id,
            customerId   = measurement.customerId,
            shoulder     = measurement.shoulder,
            chest        = measurement.chest,
            waist        = measurement.waist,
            hip          = measurement.hip,
            sleeveLength = measurement.sleeveLength,
            shirtLength  = measurement.shirtLength,
            pantLength   = measurement.pantLength,
            notes        = measurement.notes,
            updatedAt    = measurement.updatedAt
        )
        syncQueue.enqueue(measurement.id, "measurement", SyncAction.CREATE)
    }

    override suspend fun delete(id: String) {
        db.measurementsQueries.delete(id)
        syncQueue.enqueue(id, "measurement", SyncAction.DELETE)
    }
}
