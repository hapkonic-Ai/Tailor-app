package com.hapkonic.tailorapp.domain.usecase

import com.hapkonic.tailorapp.domain.model.AppUser
import com.hapkonic.tailorapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class GetCurrentUserUseCase(private val authRepository: AuthRepository) {
    /** Reactive stream — emits null when signed out. */
    operator fun invoke(): Flow<AppUser?> = authRepository.currentUser
}
