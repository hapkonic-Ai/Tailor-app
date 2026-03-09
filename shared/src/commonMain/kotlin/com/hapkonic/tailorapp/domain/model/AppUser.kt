package com.hapkonic.tailorapp.domain.model

import kotlinx.serialization.Serializable

/**
 * Authenticated user representation (Firebase UID + role claim).
 */
@Serializable
data class AppUser(
    val uid: String,
    val email: String,
    val displayName: String = "",
    val role: UserRole = UserRole.TAILOR,
    val photoUrl: String? = null
)
