package com.hapkonic.tailorapp.domain.usecase.tailor

import com.hapkonic.tailorapp.domain.model.Tailor
import com.hapkonic.tailorapp.domain.repository.TailorRepository
import kotlinx.datetime.Clock

class SaveTailorUseCase(private val repo: TailorRepository) {
    suspend operator fun invoke(tailor: Tailor) = repo.save(tailor)

    fun newTailor(name: String, phone: String, specialization: String) = Tailor(
        id             = generateId(),
        name           = name.trim(),
        phone          = phone.trim(),
        specialization = specialization.trim(),
        activeOrders   = 0,
        updatedAt      = Clock.System.now().toEpochMilliseconds()
    )

    private fun generateId() = "tailor_${Clock.System.now().toEpochMilliseconds()}"
}
