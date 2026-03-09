package com.hapkonic.tailorapp.utils

/**
 * Compresses an image from a raw ByteArray.
 * - Scales down so the longest edge does not exceed [maxEdgePx] (default 1024)
 * - Outputs WEBP (Android) / JPEG (iOS) at the given [quality] (0–100)
 */
expect class ImageCompressor() {
    suspend fun compress(bytes: ByteArray, maxEdgePx: Int = 1024, quality: Int = 85): ByteArray
}
