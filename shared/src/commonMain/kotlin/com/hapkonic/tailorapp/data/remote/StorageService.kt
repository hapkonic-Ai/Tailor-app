package com.hapkonic.tailorapp.data.remote

/**
 * Abstraction over image storage backends.
 * Implementations: FirebaseStorageService (production), LocalStorageService (dev/test).
 * Active implementation is selected via the USE_FIREBASE_STORAGE build flag.
 */
interface StorageService {
    suspend fun uploadImage(storagePath: String, imageBytes: ByteArray): String
    suspend fun deleteImage(storagePath: String)

    companion object {
        fun clothPath(orderId: String) = "orders/$orderId/cloth.webp"
        fun designPath(orderId: String) = "orders/$orderId/design.webp"
    }
}
