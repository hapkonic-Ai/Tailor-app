package com.hapkonic.tailorapp.presentation.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hapkonic.tailorapp.domain.model.AppUser
import com.hapkonic.tailorapp.domain.model.Order
import com.hapkonic.tailorapp.domain.model.OrderStatus
import com.hapkonic.tailorapp.domain.model.UserRole
import com.hapkonic.tailorapp.domain.usecase.order.GetOrderByIdUseCase
import com.hapkonic.tailorapp.domain.usecase.order.UpdateOrderStatusUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OrderDetailUiState(
    val order: Order? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val statusUpdated: Boolean = false
)

class OrderDetailViewModel(
    orderId: String,
    getOrderById: GetOrderByIdUseCase,
    private val updateOrderStatus: UpdateOrderStatusUseCase
) : ViewModel() {

    val uiState: StateFlow<OrderDetailUiState> = getOrderById(orderId)
        .map { order -> OrderDetailUiState(order = order, isLoading = false) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), OrderDetailUiState())

    private val _statusError = MutableStateFlow<String?>(null)
    val statusError: StateFlow<String?> = _statusError

    fun advanceStatus(actor: AppUser) {
        val order = uiState.value.order ?: return
        val next = order.status.next() ?: return
        updateStatus(order, next, actor)
    }

    fun setStatus(newStatus: OrderStatus, actor: AppUser) {
        val order = uiState.value.order ?: return
        updateStatus(order, newStatus, actor)
    }

    private fun updateStatus(order: Order, newStatus: OrderStatus, actor: AppUser) {
        viewModelScope.launch {
            runCatching { updateOrderStatus(order, newStatus, actor) }
                .onFailure { e -> _statusError.update { e.message } }
        }
    }

    fun clearError() = _statusError.update { null }
}
