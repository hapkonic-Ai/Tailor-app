package com.hapkonic.tailorapp.data.remote

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.storage.storage

/**
 * Handles image uploads to Firebase Storage.
 * All images are compressed before upload (Phase 6 — ImageCompressor).
 * Storage paths: orders/{orderId}/cloth.webp, orders/{orderId}/design.webp
 */
class FirebaseStorageService {

    private val storage = Firebase.storage

    /**
     * Uploads [imageBytes] to [storagePath] and returns the public download URL.
     * Recommended to call with WEBP-compressed bytes (target ~120–150 KB).
     */
    suspend fun uploadImage(storagePath: String, imageBytes: ByteArray): String {
        val ref = storage.reference(storagePath)
        ref.putBytes(imageBytes)
        return ref.getDownloadUrl()
    }

    /** Deletes the file at [storagePath]. */
    suspend fun deleteImage(storagePath: String) {
        storage.reference(storagePath).delete()
    }

    companion object {
        fun clothPath(orderId: String) = "orders/$orderId/cloth.webp"
        fun designPath(orderId: String) = "orders/$orderId/design.webp"
    }
}
