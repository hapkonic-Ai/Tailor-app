package com.hapkonic.tailorapp.presentation.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hapkonic.tailorapp.data.sync.currentTimeMillis
import com.hapkonic.tailorapp.domain.model.Customer
import com.hapkonic.tailorapp.domain.usecase.customer.GetCustomerByIdUseCase
import com.hapkonic.tailorapp.domain.usecase.customer.SaveCustomerUseCase
import com.hapkonic.tailorapp.utils.generateId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CustomerFormUiState(
    val name: String = "",
    val phone: String = "",
    val address: String = "",
    val nameError: String? = null,
    val phoneError: String? = null,
    val addressError: String? = null,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)

class CustomerFormViewModel(
    private val getCustomerById: GetCustomerByIdUseCase,
    private val saveCustomer: SaveCustomerUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CustomerFormUiState())
    val uiState: StateFlow<CustomerFormUiState> = _uiState.asStateFlow()

    private var existingCustomerId: String? = null

    /** Load an existing customer for edit mode. */
    fun loadCustomer(customerId: String) {
        existingCustomerId = customerId
        viewModelScope.launch {
            getCustomerById(customerId).firstOrNull()?.let { c ->
                _uiState.update { it.copy(name = c.name, phone = c.phone, address = c.address) }
            }
        }
    }

    fun onNameChange(v: String) = _uiState.update { it.copy(name = v, nameError = null) }
    fun onPhoneChange(v: String) = _uiState.update { it.copy(phone = v, phoneError = null) }
    fun onAddressChange(v: String) = _uiState.update { it.copy(address = v, addressError = null) }

    fun save() {
        if (!validate()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val s = _uiState.value
            val now = currentTimeMillis()
            val customer = Customer(
                id            = existingCustomerId ?: generateId(),
                name          = s.name.trim(),
                phone         = s.phone.trim(),
                address       = s.address.trim(),
                createdAt     = now,
                lastOrderDate = null,
                updatedAt     = now
            )
            runCatching { saveCustomer(customer) }
                .onSuccess { _uiState.update { it.copy(isLoading = false, isSaved = true) } }
                .onFailure { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
        }
    }

    private fun validate(): Boolean {
        val s = _uiState.value
        var ok = true
        if (s.name.isBlank()) { _uiState.update { it.copy(nameError = "Name is required") }; ok = false }
        if (s.address.isBlank()) { _uiState.update { it.copy(addressError = "Address is required") }; ok = false }
        if (!s.phone.matches(Regex("\\+?[0-9 \\-()]{7,15}"))) {
            _uiState.update { it.copy(phoneError = "Invalid phone number") }; ok = false
        }
        return ok
    }
}
