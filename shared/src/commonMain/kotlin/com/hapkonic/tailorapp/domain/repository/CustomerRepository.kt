package com.hapkonic.tailorapp.domain.repository

import com.hapkonic.tailorapp.domain.model.Customer
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {
    fun getAll(limit: Long = 20, offset: Long = 0): Flow<List<Customer>>
    fun getById(id: String): Flow<Customer?>
    fun searchByName(query: String): Flow<List<Customer>>
    fun searchByPhone(query: String): Flow<List<Customer>>
    fun getCount(): Flow<Long>
    suspend fun save(customer: Customer)
    suspend fun delete(id: String)
}
