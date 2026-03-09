package com.hapkonic.tailorapp.domain.usecase

import com.hapkonic.tailorapp.domain.model.AppUser
import com.hapkonic.tailorapp.domain.repository.AuthRepository

class SignInUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<AppUser> =
        runCatching { authRepository.signIn(email.trim(), password) }
}
