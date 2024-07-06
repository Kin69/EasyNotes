package com.kin.easynotes.presentation.components

import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import java.io.File


class GalleryObserver(handler: Handler, private val context: Context) : ContentObserver(handler) {
    override fun onChange(selfChange: Boolean, uri: Uri?) {
        super.onChange(selfChange, uri)
        uri?.let {
            deleteFileIfExists(context, getImageName(uri))
            println(getImageName(uri))
        }
    }
}

fun registerGalleryObserver(context: Context) {
    val handler = Handler(Looper.getMainLooper())
    val galleryObserver = GalleryObserver(handler, context)
    context.contentResolver.registerContentObserver(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        true,
        galleryObserver
    )
}

fun deleteFileIfExists(context: Context, fileName: String): Boolean {
    val appStorageDir = getExternalStorageDir(context)
    val file = File(appStorageDir, fileName)
    return if (file.exists()) {
        file.delete()
    } else {
        false
    }
}


fun getExternalStorageDir(context: Context): File {
    return context.getExternalFilesDir(null) ?: throw IllegalStateException("External storage directory not found")
}

fun getImageName(uri: Uri?): String {
    return if (uri != null) {
        uri.lastPathSegment?.filter { it.isDigit() }?.takeLast(10) ?: (1000..99999).random().toString()
    } else {
        (1000..99999).random().toString()
    } + ".jpg"
}