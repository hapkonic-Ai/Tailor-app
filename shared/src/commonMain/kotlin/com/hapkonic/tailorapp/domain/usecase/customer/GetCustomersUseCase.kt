package com.hapkonic.tailorapp.domain.usecase.customer

import com.hapkonic.tailorapp.domain.model.Customer
import com.hapkonic.tailorapp.domain.repository.CustomerRepository
import kotlinx.coroutines.flow.Flow

class GetCustomersUseCase(private val repo: CustomerRepository) {
    operator fun invoke(limit: Long = 20, offset: Long = 0): Flow<List<Customer>> =
        repo.getAll(limit, offset)
}
