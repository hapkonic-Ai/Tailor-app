package com.hapkonic.tailorapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
    val initialScreen: Screen = when {
        currentUser == null              -> Screen.Login
        currentUser.role == UserRole.TAILOR -> Screen.OrderList
        else                            -> Screen.Dashboard
    }
    val navigator = rememberNavigator(initialScreen)

    CompositionLocalProvider(LocalNavigator provides navigator) {
        if (navigator.current is Screen.Login) {
            LoginScreen(onSignedIn = {
                val dest = if (currentUser?.role == UserRole.TAILOR) Screen.OrderList else Screen.Dashboard
                navigator.replace(dest)
            })
        } else {
            MainScaffold(currentUser = currentUser, navigator = navigator)
        }
    }
}
