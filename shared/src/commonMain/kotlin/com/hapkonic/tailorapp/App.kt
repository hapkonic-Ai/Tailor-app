package com.hapkonic.tailorapp

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.hapkonic.tailorapp.domain.repository.AuthRepository
import com.hapkonic.tailorapp.navigation.AppNavGraph
import com.hapkonic.tailorapp.ui.theme.AppTheme
import org.koin.compose.koinInject

/**
 * Root Composable entry point, shared across Android and iOS.
 * Wraps the full navigation graph in [AppTheme].
 */
@Composable
fun App() {
    val authRepository: AuthRepository = koinInject()
    val currentUser by authRepository.currentUser.collectAsState(initial = null)

    AppTheme {
        Surface {
            AppNavGraph(currentUser = currentUser)
        }
    }
}
