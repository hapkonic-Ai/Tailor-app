package com.hapkonic.tailorapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import com.hapkonic.tailorapp.domain.model.AppUser
import com.hapkonic.tailorapp.domain.model.UserRole
import com.hapkonic.tailorapp.presentation.login.LoginScreen

/**
 * Root navigation graph.
 * - Unauthenticated → LoginScreen
 * - Authenticated   → MainScaffold (bottom nav + all screens)
 */
@Composable
fun AppNavGraph(currentUser: AppUser?) {
    val navigator = rememberNavigator(Screen.Login)

    // On logout: clear backstack so next login starts fresh
    // On login: navigate to the right root screen
    LaunchedEffect(currentUser) {
        if (currentUser == null) {
            navigator.popToRoot()
            navigator.replace(Screen.Login)
        } else if (navigator.current is Screen.Login) {
            navigator.replace(
                if (currentUser.role == UserRole.TAILOR) Screen.OrderList else Screen.Dashboard
            )
        }
    }

    CompositionLocalProvider(LocalNavigator provides navigator) {
        // Gate purely on auth state — no flash of stale screen on logout
        if (currentUser == null) {
            LoginScreen(onSignedIn = { /* navigation driven by LaunchedEffect above */ })
        } else {
            MainScaffold(currentUser = currentUser, navigator = navigator)
        }
    }
}
