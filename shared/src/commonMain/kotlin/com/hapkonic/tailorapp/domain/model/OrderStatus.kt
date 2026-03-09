package com.hapkonic.tailorapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class OrderStatus {
    PENDING, IN_PROGRESS, READY, DELIVERED;

    /** Returns the next status in the workflow, or null if already at the final step. */
    fun next(): OrderStatus? = when (this) {
        PENDING     -> IN_PROGRESS
        IN_PROGRESS -> READY
        READY       -> DELIVERED
        DELIVERED   -> null
    }
}
