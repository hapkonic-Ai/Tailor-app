package com.hapkonic.tailorapp.presentation.customer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hapkonic.tailorapp.navigation.LocalNavigator
import com.hapkonic.tailorapp.navigation.Screen
import com.hapkonic.tailorapp.ui.components.EmptyState
import com.hapkonic.tailorapp.ui.components.LoadingIndicator
import com.hapkonic.tailorapp.ui.components.OrderStatusChip
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerDetailScreen(
    customerId: String,
    viewModel: CustomerDetailViewModel = koinViewModel(parameters = { parametersOf(customerId) })
) {
    val uiState by viewModel.uiState.collectAsState()
    val navigator = LocalNavigator.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.customer?.name ?: "Customer") },
                navigationIcon = {
                    IconButton(onClick = navigator::goBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        when {
            uiState.isLoading -> LoadingIndicator()
            uiState.customer == null -> EmptyState("Customer not found")
            else -> {
                val customer = uiState.customer!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // ── Info card ─────────────────────────────────────────────
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(customer.name, style = MaterialTheme.typography.titleLarge)
                            Text(customer.phone, style = MaterialTheme.typography.bodyMedium)
                            Text(customer.address, style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    // ── Action buttons ────────────────────────────────────────
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { navigator.navigate(Screen.MeasurementForm(customerId)) },
                            modifier = Modifier.weight(1f)
                        ) { Text("Add Measurement") }
                        Button(
                            onClick = { navigator.navigate(Screen.CreateOrder(customerId)) },
                            modifier = Modifier.weight(1f)
                        ) { Text("New Order") }
                    }
                    OutlinedButton(
                        onClick = { navigator.navigate(Screen.CustomerForm(customerId)) },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Edit Customer") }

                    // ── Latest measurement ────────────────────────────────────
                    Text("Measurements", style = MaterialTheme.typography.titleMedium)
                    if (uiState.measurements.isEmpty()) {
                        Text("No measurements yet", style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    } else {
                        val m = uiState.measurements.first()
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                MeasurementRow("Shoulder", m.shoulder)
                                MeasurementRow("Chest", m.chest)
                                MeasurementRow("Waist", m.waist)
                                MeasurementRow("Hip", m.hip)
                                MeasurementRow("Sleeve", m.sleeveLength)
                            }
                        }
                    }

                    // ── Order history ─────────────────────────────────────────
                    Text("Orders", style = MaterialTheme.typography.titleMedium)
                    if (uiState.orders.isEmpty()) {
                        Text("No orders yet", style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    } else {
                        uiState.orders.forEach { order ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("₹${order.price}", style = MaterialTheme.typography.bodyMedium)
                                    OrderStatusChip(order.status)
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun MeasurementRow(label: String, value: Double) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodySmall)
        Text("${value} cm", style = MaterialTheme.typography.bodySmall)
    }
}
