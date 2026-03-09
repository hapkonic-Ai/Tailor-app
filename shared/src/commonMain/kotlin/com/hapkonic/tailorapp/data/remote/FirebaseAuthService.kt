package com.hapkonic.tailorapp.data.remote

import com.hapkonic.tailorapp.domain.model.AppUser
import com.hapkonic.tailorapp.domain.model.UserRole
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Wraps GitLive Firebase Auth.
 * Role is read from custom claims set by the Cloud Function `setUserRole`.
 */
class FirebaseAuthService {

    private val auth get() = Firebase.auth

    /** Reactive stream of the current Firebase user mapped to [AppUser]. */
    val currentUserFlow: Flow<AppUser?> = auth.authStateChanged.map { firebaseUser ->
        firebaseUser?.toAppUser()
    }

    suspend fun signIn(email: String, password: String): AppUser {
        val result = auth.signInWithEmailAndPassword(email, password)
        return result.user?.toAppUser()
            ?: throw IllegalStateException("Sign-in succeeded but user is null")
    }

    suspend fun signOut() = auth.signOut()

    suspend fun getCurrentUser(): AppUser? = auth.currentUser?.toAppUser()

    // ── Private helpers ────────────────────────────────────────────────────────

    private suspend fun FirebaseUser.toAppUser(): AppUser {
        val idToken = getIdTokenResult(false)
        val roleClaim = idToken.claims["role"] as? String
        val role = when (roleClaim) {
            "admin"  -> UserRole.ADMIN
            "tailor" -> UserRole.TAILOR
            else     -> UserRole.TAILOR   // safe default
        }
        return AppUser(
            uid         = uid,
            email       = email ?: "",
            displayName = displayName ?: "",
            role        = role,
            photoUrl    = photoURL
        )
    }
}
