package com.hapkonic.tailorapp.domain.usecase

import com.hapkonic.tailorapp.domain.repository.AuthRepository

class SignOutUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(): Unit = authRepository.signOut()
}
