package com.kin.easynotes.data.repository

import android.content.Context
import android.net.Uri
import com.kin.easynotes.core.constant.DatabaseConst
import com.kin.easynotes.data.local.database.NoteDatabaseProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class BackupRepository(
    private val provider: NoteDatabaseProvider,
    private val context: Context,
    private val mutex: Mutex,
    private val scope: CoroutineScope,
    private val dispatcher: ExecutorCoroutineDispatcher,
) {

    suspend fun export(uri: Uri) {
        withContext(dispatcher + scope.coroutineContext) {
            mutex.withLock {
                provider.close()

                context.contentResolver.openOutputStream(uri)?.use { stream ->
                    context.getDatabasePath(DatabaseConst.NOTES_DATABASE_FILE_NAME).inputStream().copyTo(stream)
                }
            }
        }
    }

    suspend fun import(uri: Uri) {
        withContext(dispatcher + scope.coroutineContext) {
            mutex.withLock {
                provider.close()

                context.contentResolver.openInputStream(uri)?.use { stream ->
                    val dbFile = context.getDatabasePath(DatabaseConst.NOTES_DATABASE_FILE_NAME)
                    dbFile?.delete()
                    stream.copyTo(dbFile.outputStream())
                }
            }
        }
    }
}