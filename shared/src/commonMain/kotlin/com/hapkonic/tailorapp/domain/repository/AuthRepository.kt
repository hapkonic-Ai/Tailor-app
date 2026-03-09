package com.hapkonic.tailorapp.domain.repository

import com.hapkonic.tailorapp.domain.model.AppUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    /** Emits the current user, or null when signed out. */
    val currentUser: Flow<AppUser?>

    /**
     * Sign in with email/password.
     * @return the authenticated [AppUser]
     * @throws Exception on auth failure
     */
    suspend fun signIn(email: String, password: String): AppUser

    /** Sign out the current user. */
    suspend fun signOut()

    /** Returns the current user synchronously, or null if not signed in. */
    suspend fun getCurrentUser(): AppUser?
}
