package com.hapkonic.tailorapp.domain.repository

import com.hapkonic.tailorapp.domain.model.Measurement
import kotlinx.coroutines.flow.Flow

interface MeasurementRepository {
    fun getByCustomer(customerId: String): Flow<List<Measurement>>
    fun getById(id: String): Flow<Measurement?>
    suspend fun save(measurement: Measurement)
    suspend fun delete(id: String)
}
