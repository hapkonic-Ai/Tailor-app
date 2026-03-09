package com.hapkonic.tailorapp.presentation.measurement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hapkonic.tailorapp.data.sync.currentTimeMillis
import com.hapkonic.tailorapp.domain.model.Measurement
import com.hapkonic.tailorapp.domain.usecase.measurement.SaveMeasurementUseCase
import com.hapkonic.tailorapp.utils.generateId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MeasurementFormUiState(
    val shoulder: String = "",
    val chest: String = "",
    val waist: String = "",
    val hip: String = "",
    val sleeveLength: String = "",
    val shirtLength: String = "",
    val pantLength: String = "",
    val notes: String = "",
    val useInches: Boolean = false,
    val errors: Map<String, String> = emptyMap(),
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)

class MeasurementFormViewModel(
    private val saveMeasurement: SaveMeasurementUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MeasurementFormUiState())
    val uiState: StateFlow<MeasurementFormUiState> = _uiState.asStateFlow()

    fun onFieldChange(field: String, value: String) {
        _uiState.update { s ->
            val errors = s.errors.toMutableMap().also { it.remove(field) }
            when (field) {
                "shoulder"    -> s.copy(shoulder = value, errors = errors)
                "chest"       -> s.copy(chest = value, errors = errors)
                "waist"       -> s.copy(waist = value, errors = errors)
                "hip"         -> s.copy(hip = value, errors = errors)
                "sleeveLength" -> s.copy(sleeveLength = value, errors = errors)
                "shirtLength" -> s.copy(shirtLength = value, errors = errors)
                "pantLength"  -> s.copy(pantLength = value, errors = errors)
                "notes"       -> s.copy(notes = value)
                else          -> s
            }
        }
    }

    fun toggleUnit() = _uiState.update { it.copy(useInches = !it.useInches) }

    fun save(customerId: String) {
        val errors = validate()
        if (errors.isNotEmpty()) { _uiState.update { it.copy(errors = errors) }; return }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val s = _uiState.value
            fun Double.toStored() = if (s.useInches) this * 2.54 else this   // always store cm

            val measurement = Measurement(
                id           = generateId(),
                customerId   = customerId,
                shoulder     = s.shoulder.toDouble().toStored(),
                chest        = s.chest.toDouble().toStored(),
                waist        = s.waist.toDouble().toStored(),
                hip          = s.hip.toDouble().toStored(),
                sleeveLength = s.sleeveLength.toDouble().toStored(),
                shirtLength  = s.shirtLength.toDouble().toStored(),
                pantLength   = s.pantLength.toDouble().toStored(),
                notes        = s.notes.ifBlank { null },
                updatedAt    = currentTimeMillis()
            )
            runCatching { saveMeasurement(measurement) }
                .onSuccess { _uiState.update { it.copy(isLoading = false, isSaved = true) } }
                .onFailure { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
        }
    }

    private fun validate(): Map<String, String> {
        val s = _uiState.value
        val unit = if (s.useInches) "in" else "cm"
        val (min, max) = if (s.useInches) 4.0 to 24.0 else 10.0 to 60.0

        return buildMap {
            listOf(
                "shoulder" to s.shoulder, "chest" to s.chest, "waist" to s.waist,
                "hip" to s.hip, "sleeveLength" to s.sleeveLength,
                "shirtLength" to s.shirtLength, "pantLength" to s.pantLength
            ).forEach { (field, raw) ->
                val v = raw.toDoubleOrNull()
                when {
                    v == null  -> put(field, "Enter a number")
                    v < min || v > max -> put(field, "Expected $min–$max $unit")
                }
            }
        }
    }
}
