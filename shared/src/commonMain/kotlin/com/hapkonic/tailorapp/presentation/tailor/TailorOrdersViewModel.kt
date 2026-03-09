package com.hapkonic.tailorapp.presentation.tailor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hapkonic.tailorapp.domain.model.AppUser
import com.hapkonic.tailorapp.domain.model.Order
import com.hapkonic.tailorapp.domain.model.OrderStatus
import com.hapkonic.tailorapp.domain.usecase.order.GetOrdersByTailorUseCase
import com.hapkonic.tailorapp.domain.usecase.order.UpdateOrderStatusUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TailorOrdersUiState(
    val ordersByStatus: Map<OrderStatus, List<Order>> = emptyMap(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class TailorOrdersViewModel(
    tailorId: String,
    getOrdersByTailor: GetOrdersByTailorUseCase,
    private val updateOrderStatus: UpdateOrderStatusUseCase
) : ViewModel() {

    val uiState: StateFlow<TailorOrdersUiState> = getOrdersByTailor(tailorId)
        .map { orders ->
            TailorOrdersUiState(
                ordersByStatus = orders
                    .filter { it.status != OrderStatus.DELIVERED }
                    .groupBy { it.status },
                isLoading = false
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TailorOrdersUiState())

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun advanceStatus(order: Order, actor: AppUser) {
        viewModelScope.launch {
            runCatching { updateOrderStatus(order, order.status.next()!!, actor) }
                .onFailure { e -> _error.update { e.message } }
        }
    }

    fun clearError() = _error.update { null }
}
