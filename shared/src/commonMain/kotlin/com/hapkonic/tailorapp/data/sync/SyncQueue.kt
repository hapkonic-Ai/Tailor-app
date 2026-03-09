package com.hapkonic.tailorapp.data.sync

import com.hapkonic.tailorapp.db.AppDatabase
import com.hapkonic.tailorapp.db.Sync_queue
import com.hapkonic.tailorapp.domain.model.SyncAction

/** Persists local writes to the sync_queue table for later remote sync. */
class SyncQueue(private val db: AppDatabase) {

    fun enqueue(entityId: String, entityType: String, action: SyncAction) {
        db.sync_queueQueries.insert(
            entityId   = entityId,
            entityType = entityType,
            action     = action.name,
            timestamp  = currentTimeMillis()
        )
    }

    fun getPending(): List<Sync_queue> =
        db.sync_queueQueries.getPending().executeAsList()

    fun markSynced(item: Sync_queue) {
        db.sync_queueQueries.markSynced(
            entityId   = item.entityId,
            entityType = item.entityType,
            action     = item.action
        )
    }

    fun incrementRetry(item: Sync_queue) {
        db.sync_queueQueries.incrementRetry(
            entityId   = item.entityId,
            entityType = item.entityType,
            action     = item.action
        )
    }

    fun deleteSynced() {
        db.sync_queueQueries.deleteSynced()
    }
}

expect fun currentTimeMillis(): Long
