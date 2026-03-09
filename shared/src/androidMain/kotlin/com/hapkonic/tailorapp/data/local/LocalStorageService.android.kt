package com.hapkonic.tailorapp.data.local

import android.content.Context
import com.hapkonic.tailorapp.data.remote.StorageService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

actual class LocalStorageService actual constructor() : StorageService, KoinComponent {

    private val context: Context by inject()

    override suspend fun uploadImage(storagePath: String, imageBytes: ByteArray): String {
        val dir = File(context.filesDir, "storage").also { it.mkdirs() }
        val file = File(dir, storagePath.replace("/", "_"))
        file.writeBytes(imageBytes)
        return file.toURI().toString()
    }

    override suspend fun deleteImage(storagePath: String) {
        File(File(context.filesDir, "storage"), storagePath.replace("/", "_")).delete()
    }
}
