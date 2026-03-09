package com.hapkonic.tailorapp.utils

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

private object AndroidCsvContext : KoinComponent {
    val context: Context by inject()
}

actual suspend fun saveCsvFile(filename: String, content: String): String =
    withContext(Dispatchers.IO) {
        val dir = AndroidCsvContext.context.getExternalFilesDir("exports")
            ?: AndroidCsvContext.context.filesDir
        dir.mkdirs()
        val file = File(dir, filename)
        file.writeText(content, Charsets.UTF_8)
        file.absolutePath
    }
