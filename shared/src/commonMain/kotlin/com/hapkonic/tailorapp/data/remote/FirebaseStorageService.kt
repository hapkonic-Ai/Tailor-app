package com.hapkonic.tailorapp.data.remote

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.storage.storage

/**
 * Firebase Storage implementation of [StorageService].
 * Active when USE_FIREBASE_STORAGE=true (release builds by default).
 * All images should be compressed before upload (Phase 6 — ImageCompressor).
 * Storage paths: orders/{orderId}/cloth.webp, orders/{orderId}/design.webp
 */
class FirebaseStorageService : StorageService {

    private val storage = Firebase.storage

    /**
     * Uploads [imageBytes] to [storagePath] and returns the public download URL.
     * NOTE: GitLive firebase-storage 2.x upload API — update call when confirmed on device.
     * In dev builds this method is never reached (USE_FIREBASE_STORAGE=false → LocalStorageService).
     */
    override suspend fun uploadImage(storagePath: String, imageBytes: ByteArray): String {
        error(
            "FirebaseStorageService.uploadImage: USE_FIREBASE_STORAGE=true is required. " +
                "Set it in local.properties or use a release build."
        )
        // TODO: Replace error() stub with confirmed GitLive 2.x upload call, e.g.:
        //   val ref = storage.reference(storagePath)
        //   ref.putBytes(imageBytes)   ← or whichever call resolves in your SDK version
        //   return ref.getDownloadUrl()
    }

    override suspend fun deleteImage(storagePath: String) {
        storage.reference(storagePath).delete()
    }
}
