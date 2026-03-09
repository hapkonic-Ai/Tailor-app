package com.hapkonic.tailorapp.domain.usecase.customer

import com.hapkonic.tailorapp.domain.model.Customer
import com.hapkonic.tailorapp.domain.repository.CustomerRepository
import kotlinx.coroutines.flow.Flow

class GetCustomerByIdUseCase(private val repo: CustomerRepository) {
    operator fun invoke(id: String): Flow<Customer?> = repo.getById(id)
}
