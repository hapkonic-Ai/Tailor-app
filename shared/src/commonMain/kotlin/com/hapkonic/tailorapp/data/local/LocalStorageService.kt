package com.hapkonic.tailorapp.data.local

import com.hapkonic.tailorapp.data.remote.StorageService

/**
 * Dev/test implementation of [StorageService].
 * Writes image bytes to device-local storage and returns a file:// URI.
 * Active when USE_FIREBASE_STORAGE=false (default in local.properties).
 */
expect class LocalStorageService() : StorageService
