package com.hapkonic.tailorapp.domain.usecase.measurement

import com.hapkonic.tailorapp.domain.model.Measurement
import com.hapkonic.tailorapp.domain.repository.MeasurementRepository
import kotlinx.coroutines.flow.Flow

class GetMeasurementsUseCase(private val repo: MeasurementRepository) {
    operator fun invoke(customerId: String): Flow<List<Measurement>> = repo.getByCustomer(customerId)
}
