package com.kin.easynotes.presentation.components

import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

// GalleryObserver class
class GalleryObserver @Inject constructor(
    handler: Handler,
    @ApplicationContext private val context: Context
) : ContentObserver(handler) {
    override fun onChange(selfChange: Boolean, uri: Uri?) {
        super.onChange(selfChange, uri)
        uri?.let {
            deleteFileIfExists(context, getImageName(uri))
        }
    }
}

// Function to unregister the GalleryObserver
fun unregisterGalleryObserver(context: Context, observer: GalleryObserver) {
    context.contentResolver.unregisterContentObserver(observer)
}

// Function to delete the file if it exists
fun deleteFileIfExists(context: Context, fileName: String): Boolean {
    val appStorageDir = getExternalStorageDir(context)
    val file = File(appStorageDir, fileName)
    return if (file.exists()) {
        file.delete()
    } else {
        false
    }
}

// Function to get the external storage directory
fun getExternalStorageDir(context: Context): File {
    return context.getExternalFilesDir(null) ?: throw IllegalStateException("External storage directory not found")
}

// Function to get the image name from the URI
fun getImageName(uri: Uri?): String {
    return if (uri != null) {
        uri.lastPathSegment?.filter { it.isDigit() }?.takeLast(10) ?: (1000..99999).random().toString()
    } else {
        (1000..99999).random().toString()
    } + ".jpg"
}
