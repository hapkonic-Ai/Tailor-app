package com.hapkonic.tailorapp.presentation.order

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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hapkonic.tailorapp.domain.model.AppUser
import com.hapkonic.tailorapp.domain.model.OrderStatus
import com.hapkonic.tailorapp.domain.model.UserRole
import com.hapkonic.tailorapp.navigation.LocalNavigator
import com.hapkonic.tailorapp.ui.components.EmptyState
import com.hapkonic.tailorapp.ui.components.LoadingIndicator
import com.hapkonic.tailorapp.ui.components.OrderStatusChip
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    orderId: String,
    currentUser: AppUser? = null,
    viewModel: OrderDetailViewModel = koinViewModel(parameters = { parametersOf(orderId) })
) {
    val uiState by viewModel.uiState.collectAsState()
    val statusError by viewModel.statusError.collectAsState()
    val navigator = LocalNavigator.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(statusError) {
        statusError?.let { snackbarHostState.showSnackbar(it); viewModel.clearError() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order Detail") },
                navigationIcon = {
                    IconButton(onClick = navigator::goBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        when {
            uiState.isLoading -> LoadingIndicator()
            uiState.order == null -> EmptyState("Order not found")
            else -> {
                val order = uiState.order!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Status banner
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("₹${order.price}", style = MaterialTheme.typography.headlineSmall)
                                Text(
                                    "Order #${order.id.take(8)}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            OrderStatusChip(order.status)
                        }
                    }

                    // Notes
                    order.notes?.let {
                        Text("Notes", style = MaterialTheme.typography.titleMedium)
                        Text(it, style = MaterialTheme.typography.bodyMedium)
                    }

                    // Status workflow buttons
                    Text("Status Actions", style = MaterialTheme.typography.titleMedium)

                    if (currentUser?.role == UserRole.ADMIN) {
                        // Admin: full status control
                        OrderStatus.entries.filter { it != order.status }.forEach { status ->
                            OutlinedButton(
                                onClick = { viewModel.setStatus(status, currentUser) },
                                modifier = Modifier.fillMaxWidth()
                            ) { Text("Set: ${status.name.replace('_', ' ')}") }
                        }
                    } else if (currentUser?.role == UserRole.TAILOR) {
                        // Tailor: advance only
                        order.status.next()?.let { next ->
                            Button(
                                onClick = { viewModel.advanceStatus(currentUser) },
                                modifier = Modifier.fillMaxWidth()
                            ) { Text("Mark as ${next.name.replace('_', ' ')}") }
                        } ?: Text(
                            "Order complete",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}
