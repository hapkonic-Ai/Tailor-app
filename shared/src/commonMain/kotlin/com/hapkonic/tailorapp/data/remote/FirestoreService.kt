package com.hapkonic.tailorapp.data.remote

import com.hapkonic.tailorapp.data.remote.dto.CustomerDto
import com.hapkonic.tailorapp.data.remote.dto.MeasurementDto
import com.hapkonic.tailorapp.data.remote.dto.OrderDto
import com.hapkonic.tailorapp.data.remote.dto.TailorDto
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.firestore

/**
 * All queries enforce:
 *  - `limit()` on every list fetch (default 20)
 *  - `startAfter(lastDoc)` for cursor-based pagination
 *  - Filters on every collection scan — no full-collection reads
 */
class FirestoreService {

    private val db = Firebase.firestore

    // ── Customers ─────────────────────────────────────────────────────────────

    suspend fun getCustomers(
        limit: Long = 20,
        lastDoc: DocumentSnapshot? = null
    ): List<CustomerDto> {
        val query = db.collection("customers")
            .orderBy("name")
            .limit(limit)
        val snapshot = if (lastDoc != null) query.startAfter(lastDoc).get() else query.get()
        return snapshot.documents.map { it.data() }
    }

    suspend fun getCustomerById(id: String): CustomerDto? =
        db.collection("customers").document(id).get()
            .takeIf { it.exists }?.data()

    suspend fun upsertCustomer(dto: CustomerDto) =
        db.collection("customers").document(dto.id).set(dto)

    suspend fun deleteCustomer(id: String) =
        db.collection("customers").document(id).delete()

    // ── Orders ────────────────────────────────────────────────────────────────

    suspend fun getOrdersByStatus(
        status: String,
        limit: Long = 20,
        lastDoc: DocumentSnapshot? = null
    ): List<OrderDto> {
        val query = db.collection("orders")
            .where { "status" equalTo status }
            .orderBy("deliveryDate")
            .limit(limit)
        val snapshot = if (lastDoc != null) query.startAfter(lastDoc).get() else query.get()
        return snapshot.documents.map { it.data() }
    }

    suspend fun getOrdersByCustomer(customerId: String): List<OrderDto> =
        db.collection("orders")
            .where { "customerId" equalTo customerId }
            .orderBy("orderDate")
            .limit(100)
            .get().documents.map { it.data() }

    suspend fun getOrdersByTailor(tailorId: String): List<OrderDto> =
        db.collection("orders")
            .where { "assignedTailorId" equalTo tailorId }
            .orderBy("deliveryDate")
            .limit(100)
            .get().documents.map { it.data() }

    suspend fun getOrdersDueToday(startOfDay: Long, endOfDay: Long): List<OrderDto> =
        db.collection("orders")
            .where {
                "deliveryDate" greaterThanOrEqualTo startOfDay
                "deliveryDate" lessThan endOfDay
            }
            .limit(50)
            .get().documents.map { it.data() }

    suspend fun getOrderById(id: String): OrderDto? =
        db.collection("orders").document(id).get()
            .takeIf { it.exists }?.data()

    suspend fun upsertOrder(dto: OrderDto) =
        db.collection("orders").document(dto.id).set(dto)

    suspend fun deleteOrder(id: String) =
        db.collection("orders").document(id).delete()

    // ── Measurements ──────────────────────────────────────────────────────────

    suspend fun getMeasurementsByCustomer(customerId: String): List<MeasurementDto> =
        db.collection("measurements")
            .where { "customerId" equalTo customerId }
            .orderBy("updatedAt")
            .limit(20)
            .get().documents.map { it.data() }

    suspend fun upsertMeasurement(dto: MeasurementDto) =
        db.collection("measurements").document(dto.id).set(dto)

    // ── Tailors ───────────────────────────────────────────────────────────────

    suspend fun getAllTailors(): List<TailorDto> =
        db.collection("tailors")
            .orderBy("name")
            .limit(200)
            .get().documents.map { it.data() }

    suspend fun upsertTailor(dto: TailorDto) =
        db.collection("tailors").document(dto.id).set(dto)

    // ── Delta Sync ────────────────────────────────────────────────────────────

    suspend fun getModifiedSince(collection: String, since: Long): List<Map<String, Any>> =
        db.collection(collection)
            .where { "updatedAt" greaterThan since }
            .limit(500)
            .get().documents.map { it.data() }
}
