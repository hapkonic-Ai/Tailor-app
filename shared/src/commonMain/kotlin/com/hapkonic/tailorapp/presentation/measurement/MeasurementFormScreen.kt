package com.hapkonic.tailorapp.presentation.measurement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.hapkonic.tailorapp.navigation.LocalNavigator
import com.hapkonic.tailorapp.ui.components.LoadingIndicator
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeasurementFormScreen(
    customerId: String,
    measurementId: String? = null,
    viewModel: MeasurementFormViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val navigator = LocalNavigator.current
    val unit = if (uiState.useInches) "in" else "cm"

    LaunchedEffect(uiState.isSaved) { if (uiState.isSaved) navigator.goBack() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Measurements") },
                navigationIcon = {
                    IconButton(onClick = navigator::goBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (uiState.isLoading) { LoadingIndicator(); return@Scaffold }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Unit toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Use inches", style = MaterialTheme.typography.bodyMedium)
                Switch(checked = uiState.useInches, onCheckedChange = { viewModel.toggleUnit() })
            }

            listOf(
                "shoulder" to "Shoulder",
                "chest" to "Chest",
                "waist" to "Waist",
                "hip" to "Hip",
                "sleeveLength" to "Sleeve Length",
                "shirtLength" to "Shirt Length",
                "pantLength" to "Pant Length"
            ).forEach { (field, label) ->
                val value = when (field) {
                    "shoulder"    -> uiState.shoulder
                    "chest"       -> uiState.chest
                    "waist"       -> uiState.waist
                    "hip"         -> uiState.hip
                    "sleeveLength" -> uiState.sleeveLength
                    "shirtLength" -> uiState.shirtLength
                    "pantLength"  -> uiState.pantLength
                    else          -> ""
                }
                OutlinedTextField(
                    value = value,
                    onValueChange = { viewModel.onFieldChange(field, it) },
                    label = { Text("$label ($unit)") },
                    isError = uiState.errors[field] != null,
                    supportingText = uiState.errors[field]?.let { { Text(it) } },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            OutlinedTextField(
                value = uiState.notes,
                onValueChange = { viewModel.onFieldChange("notes", it) },
                label = { Text("Notes") },
                minLines = 2,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { viewModel.save(customerId) },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Save Measurements") }
        }
    }
}
