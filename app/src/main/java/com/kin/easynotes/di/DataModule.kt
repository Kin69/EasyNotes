package com.kin.easynotes.di

import android.app.Application
import android.os.Handler
import android.content.Context
import android.os.Looper
import com.kin.easynotes.data.local.database.NoteDatabaseProvider
import com.kin.easynotes.data.repository.ImportExportRepository
import com.kin.easynotes.data.repository.NoteRepositoryImpl
import com.kin.easynotes.data.repository.SettingsRepositoryImpl
import com.kin.easynotes.presentation.components.EncryptionHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.sync.Mutex
import java.util.concurrent.Executors
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class WidgetCoroutineScope

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    @WidgetCoroutineScope
    fun providesWidgetCoroutineScope(): CoroutineScope = CoroutineScope(
        Executors.newSingleThreadExecutor().asCoroutineDispatcher(),
    )

    @Provides
    @Singleton
    fun provideNoteDatabaseProvider(application: Application): NoteDatabaseProvider = NoteDatabaseProvider(application)

    @Provides
    @Singleton
    fun provideCoroutineScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

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
    ): ImportExportRepository {
        return ImportExportRepository(
            provider = noteDatabaseProvider,
            context = application,
            mutex = mutex,
            scope = coroutineScope,
            dispatcher = executorCoroutineDispatcher
        )
    }

    @Provides
    @Singleton
    fun provideMutableVaultPassword(): StringBuilder {
        return StringBuilder()
    }

    @Provides
    @Singleton
    fun provideEncryptionHelper(mutableVaultPassword: StringBuilder): EncryptionHelper {
        return EncryptionHelper(mutableVaultPassword)
    }

    @Provides
    fun provideHandler(): Handler {
        return Handler(Looper.getMainLooper())
    }
}
