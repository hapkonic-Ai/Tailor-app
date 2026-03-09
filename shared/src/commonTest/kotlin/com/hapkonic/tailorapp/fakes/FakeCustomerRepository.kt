package com.hapkonic.tailorapp.fakes

import com.hapkonic.tailorapp.domain.model.Customer
import com.hapkonic.tailorapp.domain.repository.CustomerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeCustomerRepository : CustomerRepository {

    private val customers = MutableStateFlow<List<Customer>>(emptyList())

    val savedCustomers = mutableListOf<Customer>()

    fun seed(vararg customer: Customer) { customers.value = customer.toList() }

    override fun getAll(limit: Long, offset: Long): Flow<List<Customer>> = customers

    override fun getById(id: String): Flow<Customer?> =
        customers.map { list -> list.firstOrNull { it.id == id } }

    override fun searchByName(query: String): Flow<List<Customer>> =
        customers.map { list -> list.filter { it.name.contains(query, ignoreCase = true) } }

    override fun searchByPhone(query: String): Flow<List<Customer>> =
        customers.map { list -> list.filter { it.phone.contains(query) } }

    override fun getCount(): Flow<Long> = customers.map { it.size.toLong() }

    override suspend fun save(customer: Customer) {
        savedCustomers += customer
        customers.value = customers.value.filterNot { it.id == customer.id } + customer
    }

    override suspend fun delete(id: String) {
        customers.value = customers.value.filterNot { it.id == id }
    }
}
