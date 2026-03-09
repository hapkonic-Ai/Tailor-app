package com.hapkonic.tailorapp.utils

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSData
import platform.Foundation.dataWithBytes
import platform.UIKit.UIGraphicsBeginImageContextWithOptions
import platform.UIKit.UIGraphicsEndImageContext
import platform.UIKit.UIGraphicsGetImageFromCurrentImageContext
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation

@OptIn(ExperimentalForeignApi::class)
actual class ImageCompressor actual constructor() {

    actual suspend fun compress(bytes: ByteArray, maxEdgePx: Int, quality: Int): ByteArray {
        val nsData = bytes.usePinned { pinned ->
            NSData.dataWithBytes(pinned.addressOf(0), bytes.size.toULong())
        }
        val image = UIImage(data = nsData) ?: return bytes
        val scaled = scaleImage(image, maxEdgePx)
        val jpegData = UIImageJPEGRepresentation(scaled, quality.toDouble() / 100.0)
            ?: return bytes
        val result = ByteArray(jpegData.length.toInt())
        result.usePinned { pinned ->
            jpegData.getBytes(pinned.addressOf(0), jpegData.length)
        }
        return result
    }

    private fun scaleImage(image: UIImage, maxEdge: Int): UIImage {
        val w = image.size.useContents { width }
        val h = image.size.useContents { height }
        val longest = maxOf(w, h)
        if (longest <= maxEdge) return image
        val scale = maxEdge / longest
        val newSize = CGSizeMake(w * scale, h * scale)
        UIGraphicsBeginImageContextWithOptions(newSize, false, 1.0)
        image.drawInRect(CGRectMake(0.0, 0.0, newSize.useContents { width }, newSize.useContents { height }))
        val result = UIGraphicsGetImageFromCurrentImageContext() ?: image
        UIGraphicsEndImageContext()
        return result
    }
}
