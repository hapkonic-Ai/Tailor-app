package com.hapkonic.tailorapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.hapkonic.tailorapp.domain.repository.AuthRepository
import com.hapkonic.tailorapp.presentation.login.LoginScreen
import org.koin.compose.koinInject

/**
 * Root Composable entry point, shared across Android and iOS.
 *
 * Phase 3: auth-state routing — shows LoginScreen until user is signed in.
 * Phase 4: replace DashboardPlaceholder with real navigation graph.
 */
@Composable
fun App() {
    val authRepository: AuthRepository = koinInject()
    val currentUser by authRepository.currentUser.collectAsState(initial = null)

    MaterialTheme {
        Surface {
            if (currentUser == null) {
                LoginScreen(onSignedIn = { /* state update triggers recomposition */ })
            } else {
                DashboardPlaceholder()
            }
        }
    }
}

/** Placeholder until Phase 4 navigation graph is wired up. */
@Composable
private fun DashboardPlaceholder() {
    Text(text = "Welcome — Dashboard coming in Phase 4")
}
