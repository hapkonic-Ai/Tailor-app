package com.hapkonic.tailorapp.domain.usecase.measurement

import com.hapkonic.tailorapp.domain.model.Measurement
import com.hapkonic.tailorapp.domain.repository.MeasurementRepository

class SaveMeasurementUseCase(private val repo: MeasurementRepository) {
    suspend operator fun invoke(measurement: Measurement) = repo.save(measurement)
}
