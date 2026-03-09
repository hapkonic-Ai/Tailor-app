package com.hapkonic.tailorapp.data.sync

import com.hapkonic.tailorapp.data.mapper.toDomain
import com.hapkonic.tailorapp.data.mapper.toDto
import com.hapkonic.tailorapp.data.remote.FirestoreService
import com.hapkonic.tailorapp.data.remote.dto.CustomerDto
import com.hapkonic.tailorapp.data.remote.dto.MeasurementDto
import com.hapkonic.tailorapp.data.remote.dto.OrderDto
import com.hapkonic.tailorapp.data.remote.dto.TailorDto
import com.hapkonic.tailorapp.db.AppDatabase
import com.hapkonic.tailorapp.db.Sync_queue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SyncManager(
    private val db: AppDatabase,
    private val firestore: FirestoreService,
    private val syncQueue: SyncQueue,
    private val networkMonitor: NetworkMonitor,
    private val conflictResolver: ConflictResolver,
    private val scope: CoroutineScope
) {
    private val maxRetries = 5
    private var lastSyncTimestamp: Long = 0L

    fun startSync() {
        networkMonitor.startMonitoring()
        scope.launch {
            networkMonitor.isOnline.collect { isOnline ->
                if (isOnline) processQueue()
            }
        }
    }

    fun stopSync() {
        networkMonitor.stopMonitoring()
    }

    suspend fun processQueue() {
        val pending = syncQueue.getPending()

        for (item in pending) {
            try {
                syncItem(item)
                syncQueue.markSynced(item)
            } catch (e: Exception) {
                handleSyncError(item, e)
            }
        }

        syncQueue.deleteSynced()
        pullRemoteChanges()
    }

    private suspend fun syncItem(item: Sync_queue) {
        when (item.entityType) {
            "customer"    -> syncCustomer(item)
            "order"       -> syncOrder(item)
            "measurement" -> syncMeasurement(item)
            "tailor"      -> syncTailor(item)
        }
    }

    private suspend fun syncCustomer(item: Sync_queue) {
        when (item.action) {
            "DELETE" -> firestore.deleteCustomer(item.entityId)
            else     -> {
                val entity = db.customersQueries.getById(item.entityId).executeAsOneOrNull()
                    ?: return
                firestore.upsertCustomer(entity.toDomain().toDto())
            }
        }
    }

    private suspend fun syncOrder(item: Sync_queue) {
        when (item.action) {
            "DELETE" -> firestore.deleteOrder(item.entityId)
            else     -> {
                val entity = db.ordersQueries.getById(item.entityId).executeAsOneOrNull()
                    ?: return
                firestore.upsertOrder(entity.toDomain().toDto())
            }
        }
    }

    private suspend fun syncMeasurement(item: Sync_queue) {
        val entity = db.measurementsQueries.getById(item.entityId).executeAsOneOrNull()
            ?: return
        firestore.upsertMeasurement(entity.toDomain().toDto())
    }

    private suspend fun syncTailor(item: Sync_queue) {
        val entity = db.tailorsQueries.getById(item.entityId).executeAsOneOrNull()
            ?: return
        firestore.upsertTailor(entity.toDomain().toDto())
    }

    private suspend fun pullRemoteChanges() {
        val since = lastSyncTimestamp
        val now = currentTimeMillis()

        pullCustomers(since)
        pullOrders(since)
        pullMeasurements(since)
        pullTailors(since)

        lastSyncTimestamp = now
    }

    private suspend fun pullCustomers(since: Long) {
        val remote = firestore.getModifiedSince("customers", since)
        remote.forEach { map ->
            val dto = CustomerDto(
                id            = map["id"] as? String ?: return@forEach,
                name          = map["name"] as? String ?: "",
                phone         = map["phone"] as? String ?: "",
                address       = map["address"] as? String ?: "",
                createdAt     = (map["createdAt"] as? Long) ?: 0L,
                lastOrderDate = map["lastOrderDate"] as? Long,
                updatedAt     = (map["updatedAt"] as? Long) ?: 0L
            )
            val local = db.customersQueries.getById(dto.id).executeAsOneOrNull()
            if (local == null || dto.updatedAt >= local.updatedAt) {
                db.customersQueries.insert(
                    id            = dto.id,
                    name          = dto.name,
                    phone         = dto.phone,
                    address       = dto.address,
                    createdAt     = dto.createdAt,
                    lastOrderDate = dto.lastOrderDate,
                    updatedAt     = dto.updatedAt
                )
            }
        }
    }

    private suspend fun pullOrders(since: Long) {
        val remote = firestore.getModifiedSince("orders", since)
        remote.forEach { map ->
            val dto = OrderDto(
                id               = map["id"] as? String ?: return@forEach,
                customerId       = map["customerId"] as? String ?: "",
                orderDate        = (map["orderDate"] as? Long) ?: 0L,
                deliveryDate     = (map["deliveryDate"] as? Long) ?: 0L,
                status           = map["status"] as? String ?: "PENDING",
                assignedTailorId = map["assignedTailorId"] as? String ?: "",
                price            = (map["price"] as? Double) ?: 0.0,
                clothImageUrl    = map["clothImageUrl"] as? String,
                designImageUrl   = map["designImageUrl"] as? String,
                notes            = map["notes"] as? String,
                updatedAt        = (map["updatedAt"] as? Long) ?: 0L
            )
            val local = db.ordersQueries.getById(dto.id).executeAsOneOrNull()
            if (local == null || dto.updatedAt >= local.updatedAt) {
                db.ordersQueries.insert(
                    id               = dto.id,
                    customerId       = dto.customerId,
                    orderDate        = dto.orderDate,
                    deliveryDate     = dto.deliveryDate,
                    status           = dto.status,
                    assignedTailorId = dto.assignedTailorId,
                    price            = dto.price,
                    clothImageUrl    = dto.clothImageUrl,
                    designImageUrl   = dto.designImageUrl,
                    notes            = dto.notes,
                    updatedAt        = dto.updatedAt
                )
            }
        }
    }

    private suspend fun pullMeasurements(since: Long) {
        val remote = firestore.getModifiedSince("measurements", since)
        remote.forEach { map ->
            val dto = MeasurementDto(
                id           = map["id"] as? String ?: return@forEach,
                customerId   = map["customerId"] as? String ?: "",
                shoulder     = (map["shoulder"] as? Double) ?: 0.0,
                chest        = (map["chest"] as? Double) ?: 0.0,
                waist        = (map["waist"] as? Double) ?: 0.0,
                hip          = (map["hip"] as? Double) ?: 0.0,
                sleeveLength = (map["sleeveLength"] as? Double) ?: 0.0,
                shirtLength  = (map["shirtLength"] as? Double) ?: 0.0,
                pantLength   = (map["pantLength"] as? Double) ?: 0.0,
                notes        = map["notes"] as? String,
                updatedAt    = (map["updatedAt"] as? Long) ?: 0L
            )
            val local = db.measurementsQueries.getById(dto.id).executeAsOneOrNull()
            if (local == null || dto.updatedAt >= local.updatedAt) {
                db.measurementsQueries.insert(
                    id           = dto.id,
                    customerId   = dto.customerId,
                    shoulder     = dto.shoulder,
                    chest        = dto.chest,
                    waist        = dto.waist,
                    hip          = dto.hip,
                    sleeveLength = dto.sleeveLength,
                    shirtLength  = dto.shirtLength,
                    pantLength   = dto.pantLength,
                    notes        = dto.notes,
                    updatedAt    = dto.updatedAt
                )
            }
        }
    }

    private suspend fun pullTailors(since: Long) {
        val remote = firestore.getModifiedSince("tailors", since)
        remote.forEach { map ->
            val dto = TailorDto(
                id             = map["id"] as? String ?: return@forEach,
                name           = map["name"] as? String ?: "",
                phone          = map["phone"] as? String ?: "",
                specialization = map["specialization"] as? String ?: "",
                activeOrders   = (map["activeOrders"] as? Long)?.toInt() ?: 0,
                updatedAt      = (map["updatedAt"] as? Long) ?: 0L
            )
            val local = db.tailorsQueries.getById(dto.id).executeAsOneOrNull()
            if (local == null || dto.updatedAt >= local.updatedAt) {
                db.tailorsQueries.insert(
                    id             = dto.id,
                    name           = dto.name,
                    phone          = dto.phone,
                    specialization = dto.specialization,
                    activeOrders   = dto.activeOrders.toLong(),
                    updatedAt      = dto.updatedAt
                )
            }
        }
    }

    private suspend fun handleSyncError(item: Sync_queue, error: Exception) {
        val retryCount = item.retryCount.toInt()
        if (retryCount < maxRetries) {
            syncQueue.incrementRetry(item)
            // Exponential backoff: 1s, 2s, 4s, 8s, 16s — capped at 30s
            delay((1000L * (1L shl retryCount)).coerceAtMost(30_000L))
        } else {
            // Log permanently failed sync — surfaced to admin in Phase 5 reporting
            println("[SyncManager] PERMANENT FAILURE: ${item.entityType}/${item.entityId} — ${error.message}")
        }
    }
}
