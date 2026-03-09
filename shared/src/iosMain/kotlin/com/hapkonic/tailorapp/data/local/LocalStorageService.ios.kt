package com.hapkonic.tailorapp.data.local

import com.hapkonic.tailorapp.data.remote.StorageService
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import platform.Foundation.dataWithBytes

@OptIn(ExperimentalForeignApi::class)
actual class LocalStorageService actual constructor() : StorageService {

    override suspend fun uploadImage(storagePath: String, imageBytes: ByteArray): String {
        val docs = NSFileManager.defaultManager.URLForDirectory(
            NSDocumentDirectory, NSUserDomainMask, null, true, null
        ) ?: return ""
        val file = docs.URLByAppendingPathComponent(storagePath.replace("/", "_")) ?: return ""
        NSData.dataWithBytes(imageBytes.refTo(0), imageBytes.size.toULong()).writeToURL(file, true)
        return file.absoluteString ?: ""
    }

    override suspend fun deleteImage(storagePath: String) {
        val docs = NSFileManager.defaultManager.URLForDirectory(
            NSDocumentDirectory, NSUserDomainMask, null, true, null
        ) ?: return
        val file = docs.URLByAppendingPathComponent(storagePath.replace("/", "_")) ?: return
        NSFileManager.defaultManager.removeItemAtURL(file, null)
    }
}
