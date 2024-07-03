package com.kin.easynotes.di

import android.app.Application
import android.content.Context
import com.kin.easynotes.data.local.database.NoteDatabaseProvider
import com.kin.easynotes.data.repository.BackupRepository
import com.kin.easynotes.data.repository.NoteRepositoryImpl
import com.kin.easynotes.data.repository.SettingsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.sync.Mutex
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideKeyDatabaseProvider(application: Application): NoteDatabaseProvider = NoteDatabaseProvider(application)

    @Provides
    @Singleton
    fun provideCoroutineScope(): CoroutineScope = CoroutineScope(SupervisorJob())

    @Provides
    fun provideMutex(): Mutex = Mutex()

    @Provides
    @Singleton
    fun provideExecutorCoroutineDispatcher(): ExecutorCoroutineDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    @Provides
    @Singleton
    fun provideNoteRepository(noteDatabaseProvider: NoteDatabaseProvider): NoteRepositoryImpl {
        return NoteRepositoryImpl(noteDatabaseProvider)
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(@ApplicationContext context: Context): SettingsRepositoryImpl {
        return SettingsRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideBackupRepository(
        noteDatabaseProvider: NoteDatabaseProvider,
        application: Application,
        mutex: Mutex,
        coroutineScope: CoroutineScope,
        executorCoroutineDispatcher: ExecutorCoroutineDispatcher,
    ): BackupRepository {
        return BackupRepository(
            provider = noteDatabaseProvider,
            context = application,
            mutex = mutex,
            scope = coroutineScope,
            dispatcher = executorCoroutineDispatcher
        )
    }
}
