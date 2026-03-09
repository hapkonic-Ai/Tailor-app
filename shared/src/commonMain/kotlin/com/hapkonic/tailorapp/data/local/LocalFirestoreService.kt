package com.hapkonic.tailorapp.data.local

import com.hapkonic.tailorapp.data.remote.IFirestoreService
import com.hapkonic.tailorapp.data.remote.dto.CustomerDto
import com.hapkonic.tailorapp.data.remote.dto.MeasurementDto
import com.hapkonic.tailorapp.data.remote.dto.OrderDto
import com.hapkonic.tailorapp.data.remote.dto.TailorDto
import dev.gitlive.firebase.firestore.DocumentSnapshot

/**
 * No-op Firestore stub for dev/offline builds (USE_FIREBASE_STORAGE=false).
 * All reads return empty results; all writes are silent no-ops.
 * The app works entirely from SQLDelight local DB in this mode.
 */
class LocalFirestoreService : IFirestoreService {
    override suspend fun getCustomers(limit: Long, lastDoc: DocumentSnapshot?) = emptyList<CustomerDto>()
    override suspend fun getCustomerById(id: String): CustomerDto? = null
    override suspend fun upsertCustomer(dto: CustomerDto) = Unit
    override suspend fun deleteCustomer(id: String) = Unit

    override suspend fun getOrdersByStatus(status: String, limit: Long, lastDoc: DocumentSnapshot?) = emptyList<OrderDto>()
    override suspend fun getOrdersByCustomer(customerId: String) = emptyList<OrderDto>()
    override suspend fun getOrdersByTailor(tailorId: String) = emptyList<OrderDto>()
    override suspend fun getOrdersDueToday(startOfDay: Long, endOfDay: Long) = emptyList<OrderDto>()
    override suspend fun getOrderById(id: String): OrderDto? = null
    override suspend fun upsertOrder(dto: OrderDto) = Unit
    override suspend fun deleteOrder(id: String) = Unit

    override suspend fun getMeasurementsByCustomer(customerId: String) = emptyList<MeasurementDto>()
    override suspend fun upsertMeasurement(dto: MeasurementDto) = Unit

    override suspend fun getAllTailors() = emptyList<TailorDto>()
    override suspend fun upsertTailor(dto: TailorDto) = Unit

    override suspend fun getModifiedSince(collection: String, since: Long) = emptyList<Map<String, Any>>()
}
