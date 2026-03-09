package com.hapkonic.tailorapp.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.NSUserDomainMask
import platform.Foundation.stringByAppendingPathComponent
import platform.Foundation.writeToFile

actual suspend fun saveCsvFile(filename: String, content: String): String =
    withContext(Dispatchers.Default) {
        val dirs = NSSearchPathForDirectoriesInDomains(
            NSDocumentDirectory,
            NSUserDomainMask,
            true
        )
        val docDir = dirs.firstOrNull() as? String
            ?: NSFileManager.defaultManager.currentDirectoryPath
        val path = (docDir as NSString).stringByAppendingPathComponent(filename)
        (content as NSString).writeToFile(
            path,
            atomically = true,
            encoding = NSUTF8StringEncoding,
            error = null
        )
        path
    }
