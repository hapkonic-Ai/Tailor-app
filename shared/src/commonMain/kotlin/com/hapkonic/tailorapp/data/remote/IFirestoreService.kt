package com.hapkonic.tailorapp.data.remote

import com.hapkonic.tailorapp.data.remote.dto.CustomerDto
import com.hapkonic.tailorapp.data.remote.dto.MeasurementDto
import com.hapkonic.tailorapp.data.remote.dto.OrderDto
import com.hapkonic.tailorapp.data.remote.dto.TailorDto
import dev.gitlive.firebase.firestore.DocumentSnapshot

/**
 * Abstraction over Firestore remote data operations.
 * FirestoreService — production (requires Firebase initialized)
 * LocalFirestoreService — dev/offline no-op stub
 */
interface IFirestoreService {
    suspend fun getCustomers(limit: Long = 20, lastDoc: DocumentSnapshot? = null): List<CustomerDto>
    suspend fun getCustomerById(id: String): CustomerDto?
    suspend fun upsertCustomer(dto: CustomerDto)
    suspend fun deleteCustomer(id: String)

    suspend fun getOrdersByStatus(status: String, limit: Long = 20, lastDoc: DocumentSnapshot? = null): List<OrderDto>
    suspend fun getOrdersByCustomer(customerId: String): List<OrderDto>
    suspend fun getOrdersByTailor(tailorId: String): List<OrderDto>
    suspend fun getOrdersDueToday(startOfDay: Long, endOfDay: Long): List<OrderDto>
    suspend fun getOrderById(id: String): OrderDto?
    suspend fun upsertOrder(dto: OrderDto)
    suspend fun deleteOrder(id: String)

    suspend fun getMeasurementsByCustomer(customerId: String): List<MeasurementDto>
    suspend fun upsertMeasurement(dto: MeasurementDto)

    suspend fun getAllTailors(): List<TailorDto>
    suspend fun upsertTailor(dto: TailorDto)

    suspend fun getModifiedSince(collection: String, since: Long): List<Map<String, Any>>
}
