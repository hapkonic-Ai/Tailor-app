package com.hapkonic.tailorapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SyncMetadata(
    val entityId: String,
    val entityType: String,   // "customer" | "order" | "measurement" | "tailor"
    val action: SyncAction,
    val timestamp: Long,
    val synced: Boolean,
    val retryCount: Int = 0
)

@Serializable
enum class SyncAction { CREATE, UPDATE, DELETE }

/** Implemented by all domain models so ConflictResolver can compare them. */
interface HasTimestamp {
    val updatedAt: Long
}
