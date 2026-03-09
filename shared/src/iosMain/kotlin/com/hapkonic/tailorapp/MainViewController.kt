package com.hapkonic.tailorapp

import androidx.compose.ui.window.ComposeUIViewController
import com.hapkonic.tailorapp.di.appModule
import com.hapkonic.tailorapp.di.platformModule
import org.koin.core.context.startKoin

/**
 * Entry point called from iOS Swift code (ContentView.swift).
 * Initializes Koin and returns the root Compose UIViewController.
 */
fun MainViewController() = ComposeUIViewController(
    configure = {
        startKoin {
            modules(platformModule, appModule)
        }
    }
) {
    App()
}
