package com.hapkonic.tailorapp.presentation.customer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hapkonic.tailorapp.domain.model.AppUser
import com.hapkonic.tailorapp.domain.model.Customer
import com.hapkonic.tailorapp.domain.model.UserRole
import com.hapkonic.tailorapp.navigation.LocalNavigator
import com.hapkonic.tailorapp.navigation.Screen
import com.hapkonic.tailorapp.ui.components.EmptyState
import com.hapkonic.tailorapp.ui.components.LoadingIndicator
import com.hapkonic.tailorapp.ui.components.SearchBar
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerListScreen(
    currentUser: AppUser? = null,
    viewModel: CustomerListViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val navigator = LocalNavigator.current

    Scaffold(
        topBar = { TopAppBar(title = { Text("Customers") }) },
        floatingActionButton = {
            if (currentUser?.role == UserRole.ADMIN) {
                FloatingActionButton(onClick = { navigator.navigate(Screen.CustomerForm()) }) {
                    Icon(Icons.Default.Add, contentDescription = "Add customer")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            SearchBar(
                query = uiState.query,
                onQueryChange = viewModel::onQueryChange,
                placeholder = "Search by name or phone",
                modifier = Modifier.padding(vertical = 8.dp)
            )

            when {
                uiState.isLoading -> LoadingIndicator()
                uiState.customers.isEmpty() -> EmptyState("No customers found")
                else -> LazyColumn(
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.customers) { customer ->
                        CustomerListItem(
                            customer = customer,
                            onClick = { navigator.navigate(Screen.CustomerDetail(customer.id)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomerListItem(customer: Customer, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(customer.name, style = MaterialTheme.typography.titleMedium)
                Text(customer.phone, style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (customer.lastOrderDate != null) {
                Text(
                    text = "Last order",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
