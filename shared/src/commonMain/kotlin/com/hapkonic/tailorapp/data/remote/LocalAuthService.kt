package com.hapkonic.tailorapp.data.remote

import com.hapkonic.tailorapp.domain.model.AppUser
import com.hapkonic.tailorapp.domain.model.UserRole
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Simple local credential-based authentication — no Firebase Auth.
 *
 * Credentials are stored in-memory. Seed accounts are provided below.
 * Phase 6: replace seeded map with a SQLDelight-backed user table + bcrypt hashing.
 *
 * Default accounts (change these before production):
 *   admin@tailorapp.com / admin123  → ADMIN
 *   tailor@tailorapp.com / tailor123 → TAILOR
 */
class LocalAuthService {

    private val _currentUser = MutableStateFlow<AppUser?>(null)
    val currentUserFlow: Flow<AppUser?> = _currentUser.asStateFlow()

    // email.lowercase() → Pair(password, role)
    private val credentials = mutableMapOf(
        "admin@tailorapp.com"  to Pair("admin123",  UserRole.ADMIN),
        "tailor@tailorapp.com" to Pair("tailor123", UserRole.TAILOR)
    )

    /**
     * Validates credentials and sets [currentUserFlow] on success.
     * @throws AuthException on bad credentials.
     */
    fun signIn(email: String, password: String): AppUser {
        val key = email.trim().lowercase()
        val (stored, role) = credentials[key]
            ?: throw AuthException("Invalid email or password.")
        if (stored != password) throw AuthException("Invalid email or password.")

        val user = AppUser(
            uid         = key,
            email       = key,
            displayName = key.substringBefore("@").replaceFirstChar { it.uppercase() },
            role        = role
        )
        _currentUser.value = user
        return user
    }

    fun signOut() {
        _currentUser.value = null
    }

    fun getCurrentUser(): AppUser? = _currentUser.value
}

class AuthException(message: String) : Exception(message)
