package com.hapkonic.tailorapp.data.repository

import com.hapkonic.tailorapp.data.remote.LocalAuthService
import com.hapkonic.tailorapp.domain.model.AppUser
import com.hapkonic.tailorapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class AuthRepositoryImpl(
    private val authService: LocalAuthService
) : AuthRepository {

    override val currentUser: Flow<AppUser?> = authService.currentUserFlow

    override suspend fun signIn(email: String, password: String): AppUser =
        authService.signIn(email, password)

    override suspend fun signOut() = authService.signOut()

    override suspend fun getCurrentUser(): AppUser? = authService.getCurrentUser()

    override suspend fun signInWithBiometric(): AppUser? = authService.signInWithBiometric()
}
