package com.aralhub.araltaxi.core.common.file

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object UriEx {
    fun getFileFromUri(context: Context, uri: Uri): File? {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir)
        inputStream?.use { input -> FileOutputStream(tempFile).use { output -> input.copyTo(output) } }
        return tempFile
    }
}