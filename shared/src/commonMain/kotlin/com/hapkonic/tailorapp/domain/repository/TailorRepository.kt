package com.hapkonic.tailorapp.domain.repository

import com.hapkonic.tailorapp.domain.model.Tailor
import kotlinx.coroutines.flow.Flow

interface TailorRepository {
    fun getAll(): Flow<List<Tailor>>
    fun getById(id: String): Flow<Tailor?>
    suspend fun save(tailor: Tailor)
    suspend fun delete(id: String)
}
