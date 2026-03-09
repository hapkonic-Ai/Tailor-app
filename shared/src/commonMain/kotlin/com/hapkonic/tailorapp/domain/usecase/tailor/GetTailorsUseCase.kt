package com.hapkonic.tailorapp.domain.usecase.tailor

import com.hapkonic.tailorapp.domain.model.Tailor
import com.hapkonic.tailorapp.domain.repository.TailorRepository
import kotlinx.coroutines.flow.Flow

class GetTailorsUseCase(private val repo: TailorRepository) {
    operator fun invoke(): Flow<List<Tailor>> = repo.getAll()
}
