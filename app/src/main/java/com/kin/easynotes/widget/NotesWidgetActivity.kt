package com.kin.easynotes.widget

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.kin.easynotes.R
import com.kin.easynotes.domain.usecase.NoteUseCase
import com.kin.easynotes.presentation.components.NotesScaffold
import com.kin.easynotes.presentation.screens.home.getContainerColor
import com.kin.easynotes.presentation.screens.home.sorter
import com.kin.easynotes.presentation.screens.home.widgets.NoteFilter
import com.kin.easynotes.presentation.screens.settings.TopBar
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel
import com.kin.easynotes.presentation.screens.settings.settings.shapeManager
import com.kin.easynotes.presentation.theme.LeafNotesTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotesWidgetActivity : ComponentActivity() {

    @Inject
    lateinit var noteUseCase: NoteUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()

        val appWidgetId = intent?.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID,
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        val resultValue = Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_CANCELED, resultValue)

        setContent {
            val settings = hiltViewModel<SettingsViewModel>()
            val containerColor = getContainerColor(settings)
            val coroutineScope = MainScope()

            noteUseCase.observe()

            LeafNotesTheme(settingsModel = settings) {
                NotesScaffold(
                    topBar = {
                        TopBar(
                            title =  stringResource(id = R.string.select_note),
                            onBackNavClicked = { finish() }
                        )
                    }
                ) {
                    NoteFilter(
                        settingsViewModel = settings,
                        containerColor = containerColor,
                        shape = shapeManager(radius = settings.settings.value.cornerRadius / 2, isBoth = true),
                        onNoteClicked = { id ->
                            coroutineScope.launch {
                                NotesWidgetReceiver.observeData(this@NotesWidgetActivity, id)
                                val resultValue = Intent().putExtra(
                                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                                    appWidgetId,
                                )
                                setResult(RESULT_OK, resultValue)
                                finish()
                            }
                        },
                        notes = noteUseCase.notes.sortedWith(sorter(settings.settings.value.sortDescending)),
                        viewMode = false,
                    )
                }
            }
        }
    }
}