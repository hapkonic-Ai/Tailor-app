package com.hapkonic.tailorapp.di

/**
 * Controls which StorageService implementation Koin injects.
 * false → LocalStorageService (dev/test default)
 * true  → FirebaseStorageService (release builds)
 *
 * Android: driven by BuildConfig.USE_FIREBASE_STORAGE (set via local.properties or buildTypes)
 * iOS: set at compile time in StorageConfig.ios.kt
 */
expect val useFirebaseStorage: Boolean
