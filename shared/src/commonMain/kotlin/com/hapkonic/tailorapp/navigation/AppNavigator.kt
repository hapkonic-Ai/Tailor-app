package com.hapkonic.tailorapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember

/** Simple backstack-based navigator for Compose Multiplatform. */
class AppNavigator(initialScreen: Screen) {
    private val backstack = mutableStateListOf(initialScreen)

    val current: Screen get() = backstack.last()
    val canGoBack: Boolean get() = backstack.size > 1

    fun navigate(screen: Screen) {
        backstack.add(screen)
    }

    /** Replace the current screen (no backstack entry added). */
    fun replace(screen: Screen) {
        backstack[backstack.lastIndex] = screen
    }

    fun goBack() {
        if (canGoBack) backstack.removeLast()
    }

    /** Pop to root (Login or the first screen). */
    fun popToRoot() {
        while (backstack.size > 1) backstack.removeLast()
    }
}

val LocalNavigator = compositionLocalOf<AppNavigator> {
    error("No AppNavigator provided")
}

@Composable
fun rememberNavigator(initialScreen: Screen): AppNavigator =
    remember { AppNavigator(initialScreen) }
