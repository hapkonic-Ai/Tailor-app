package com.hapkonic.tailorapp.presentation.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.hapkonic.tailorapp.navigation.LocalNavigator
import com.hapkonic.tailorapp.ui.components.LoadingIndicator
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOrderScreen(
    preselectedCustomerId: String? = null,
    viewModel: CreateOrderViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val navigator = LocalNavigator.current

    LaunchedEffect(preselectedCustomerId) {
        preselectedCustomerId?.let { viewModel.setCustomer(it) }
    }
    LaunchedEffect(uiState.isSaved) { if (uiState.isSaved) navigator.goBack() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Order") },
                navigationIcon = {
                    IconButton(onClick = navigator::goBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (uiState.isLoading) { LoadingIndicator(); return@Scaffold }

        var tailorMenuExpanded by remember { mutableStateOf(false) }
        val selectedTailor = uiState.tailors.find { it.id == uiState.selectedTailorId }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Tailor picker
            ExposedDropdownMenuBox(
                expanded = tailorMenuExpanded,
                onExpandedChange = { tailorMenuExpanded = it }
            ) {
                OutlinedTextField(
                    value = selectedTailor?.name ?: "Select tailor",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Assign Tailor *") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(tailorMenuExpanded) },
                    isError = uiState.tailorError != null,
                    supportingText = uiState.tailorError?.let { { Text(it) } },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = tailorMenuExpanded,
                    onDismissRequest = { tailorMenuExpanded = false }
                ) {
                    uiState.tailors.forEach { tailor ->
                        DropdownMenuItem(
                            text = { Text("${tailor.name} (${tailor.activeOrders} active)") },
                            onClick = {
                                viewModel.onTailorSelected(tailor.id)
                                tailorMenuExpanded = false
                            }
                        )
                    }
                }
            }

            // Price
            OutlinedTextField(
                value = uiState.price,
                onValueChange = viewModel::onPriceChange,
                label = { Text("Price (₹) *") },
                isError = uiState.priceError != null,
                supportingText = uiState.priceError?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            // Notes
            OutlinedTextField(
                value = uiState.notes,
                onValueChange = viewModel::onNotesChange,
                label = { Text("Notes") },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = viewModel::save,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Create Order") }
        }
    }
}
