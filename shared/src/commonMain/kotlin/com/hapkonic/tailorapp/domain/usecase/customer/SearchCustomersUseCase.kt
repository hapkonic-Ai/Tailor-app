package com.hapkonic.tailorapp.domain.usecase.customer

import com.hapkonic.tailorapp.domain.model.Customer
import com.hapkonic.tailorapp.domain.repository.CustomerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class SearchCustomersUseCase(private val repo: CustomerRepository) {
    /** Searches by name and phone simultaneously and merges results (deduped by id). */
    operator fun invoke(query: String): Flow<List<Customer>> {
        if (query.isBlank()) return repo.getAll()
        return combine(
            repo.searchByName(query),
            repo.searchByPhone(query)
        ) { byName, byPhone ->
            (byName + byPhone).distinctBy { it.id }
        }
    }
}
