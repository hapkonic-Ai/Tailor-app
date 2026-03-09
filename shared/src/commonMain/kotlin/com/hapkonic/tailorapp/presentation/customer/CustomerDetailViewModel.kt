package com.hapkonic.tailorapp.presentation.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hapkonic.tailorapp.domain.model.Customer
import com.hapkonic.tailorapp.domain.model.Measurement
import com.hapkonic.tailorapp.domain.model.Order
import com.hapkonic.tailorapp.domain.usecase.customer.GetCustomerByIdUseCase
import com.hapkonic.tailorapp.domain.usecase.measurement.GetMeasurementsUseCase
import com.hapkonic.tailorapp.domain.usecase.order.GetOrdersByCustomerUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class CustomerDetailUiState(
    val customer: Customer? = null,
    val measurements: List<Measurement> = emptyList(),
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = true
)

class CustomerDetailViewModel(
    customerId: String,
    getCustomerById: GetCustomerByIdUseCase,
    getMeasurements: GetMeasurementsUseCase,
    getOrdersByCustomer: GetOrdersByCustomerUseCase
) : ViewModel() {

    val uiState: StateFlow<CustomerDetailUiState> = combine(
        getCustomerById(customerId),
        getMeasurements(customerId),
        getOrdersByCustomer(customerId)
    ) { customer, measurements, orders ->
        CustomerDetailUiState(
            customer     = customer,
            measurements = measurements,
            orders       = orders.sortedByDescending { it.orderDate },
            isLoading    = false
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CustomerDetailUiState())
}
