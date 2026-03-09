package com.hapkonic.tailorapp.presentation.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hapkonic.tailorapp.data.sync.currentTimeMillis
import com.hapkonic.tailorapp.domain.model.Order
import com.hapkonic.tailorapp.domain.model.OrderStatus
import com.hapkonic.tailorapp.domain.model.Tailor
import com.hapkonic.tailorapp.domain.usecase.order.CreateOrderUseCase
import com.hapkonic.tailorapp.domain.usecase.tailor.GetTailorsUseCase
import com.hapkonic.tailorapp.utils.generateId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CreateOrderUiState(
    val customerId: String = "",
    val tailors: List<Tailor> = emptyList(),
    val selectedTailorId: String = "",
    val deliveryDate: Long = 0L,
    val price: String = "",
    val notes: String = "",
    val priceError: String? = null,
    val tailorError: String? = null,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)

class CreateOrderViewModel(
    private val createOrder: CreateOrderUseCase,
    getTailors: GetTailorsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateOrderUiState())
    val uiState: StateFlow<CreateOrderUiState> = _uiState.asStateFlow()

    init {
        getTailors().onEach { list ->
            _uiState.update { it.copy(tailors = list) }
        }.launchIn(viewModelScope)
    }

    fun setCustomer(customerId: String) = _uiState.update { it.copy(customerId = customerId) }
    fun onTailorSelected(tailorId: String) = _uiState.update { it.copy(selectedTailorId = tailorId, tailorError = null) }
    fun onDeliveryDateChange(ts: Long) = _uiState.update { it.copy(deliveryDate = ts) }
    fun onPriceChange(v: String) = _uiState.update { it.copy(price = v, priceError = null) }
    fun onNotesChange(v: String) = _uiState.update { it.copy(notes = v) }

    fun save() {
        val s = _uiState.value
        val price = s.price.toDoubleOrNull()
        if (price == null || price <= 0) {
            _uiState.update { it.copy(priceError = "Enter a valid price") }
            return
        }
        if (s.selectedTailorId.isBlank()) {
            _uiState.update { it.copy(tailorError = "Select a tailor") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val now = currentTimeMillis()
            val order = Order(
                id               = generateId(),
                customerId       = s.customerId,
                orderDate        = now,
                deliveryDate     = if (s.deliveryDate > 0) s.deliveryDate else now + 7 * 24 * 60 * 60 * 1000L,
                status           = OrderStatus.PENDING,
                assignedTailorId = s.selectedTailorId,
                price            = price,
                clothImageUrl    = null,
                designImageUrl   = null,
                notes            = s.notes.ifBlank { null },
                updatedAt        = now
            )
            runCatching { createOrder(order) }
                .onSuccess { _uiState.update { it.copy(isLoading = false, isSaved = true) } }
                .onFailure { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
        }
    }
}
