package com.hapkonic.tailorapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Root Composable entry point, shared across Android and iOS.
 * In Phase 4 this will be replaced with the full navigation graph.
 */
@Composable
fun App() {
    MaterialTheme {
        Surface {
            Text(
                text = "Tailor App — Phase 1",
                modifier = Modifier
            )
        }
    }
}
