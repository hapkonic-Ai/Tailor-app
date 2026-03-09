package com.hapkonic.tailorapp.domain.usecase.customer

import com.hapkonic.tailorapp.domain.model.Customer
import com.hapkonic.tailorapp.domain.repository.CustomerRepository

/** Handles both add and update — caller builds the Customer object. */
class SaveCustomerUseCase(private val repo: CustomerRepository) {
    suspend operator fun invoke(customer: Customer) = repo.save(customer)
}
