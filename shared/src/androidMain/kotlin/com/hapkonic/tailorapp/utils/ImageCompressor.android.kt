package com.hapkonic.tailorapp.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import java.io.ByteArrayOutputStream

actual class ImageCompressor actual constructor() {

    actual suspend fun compress(bytes: ByteArray, maxEdgePx: Int, quality: Int): ByteArray {
        val original = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            ?: return bytes
        val scaled = scaleBitmap(original, maxEdgePx)
        val format = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            Bitmap.CompressFormat.WEBP_LOSSY
        else
            @Suppress("DEPRECATION")
            Bitmap.CompressFormat.WEBP
        val out = ByteArrayOutputStream()
        scaled.compress(format, quality, out)
        return out.toByteArray()
    }

    private fun scaleBitmap(bitmap: Bitmap, maxEdge: Int): Bitmap {
        val w = bitmap.width
        val h = bitmap.height
        val longest = maxOf(w, h)
        if (longest <= maxEdge) return bitmap
        val scale = maxEdge.toFloat() / longest
        return Bitmap.createScaledBitmap(
            bitmap,
            (w * scale).toInt(),
            (h * scale).toInt(),
            true
        )
    }
}
