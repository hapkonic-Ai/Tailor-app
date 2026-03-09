package com.hapkonic.tailorapp.presentation.customer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.hapkonic.tailorapp.navigation.LocalNavigator
import com.hapkonic.tailorapp.ui.components.LoadingIndicator
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerFormScreen(
    customerId: String? = null,
    viewModel: CustomerFormViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val navigator = LocalNavigator.current

    LaunchedEffect(customerId) { customerId?.let { viewModel.loadCustomer(it) } }
    LaunchedEffect(uiState.isSaved) { if (uiState.isSaved) navigator.goBack() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (customerId == null) "Add Customer" else "Edit Customer") },
                navigationIcon = {
                    IconButton(onClick = navigator::goBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            LoadingIndicator()
            return@Scaffold
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = uiState.name,
                onValueChange = viewModel::onNameChange,
                label = { Text("Name *") },
                isError = uiState.nameError != null,
                supportingText = uiState.nameError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.phone,
                onValueChange = viewModel::onPhoneChange,
                label = { Text("Phone *") },
                isError = uiState.phoneError != null,
                supportingText = uiState.phoneError?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = uiState.address,
                onValueChange = viewModel::onAddressChange,
                label = { Text("Address *") },
                isError = uiState.addressError != null,
                supportingText = uiState.addressError?.let { { Text(it) } },
                minLines = 2,
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = viewModel::save,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Save") }
        }
    }
}
