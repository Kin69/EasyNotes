package com.kin.easynotes.presentation.screens.settings

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kin.easynotes.domain.usecase.NoteViewModel
import com.kin.easynotes.presentation.components.AppBarView
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel
import com.kin.easynotes.presentation.screens.settings.widgets.SettingSection
import com.kin.easynotes.presentation.screens.settings.widgets.SettingsBox


@Composable
fun SettingsView(
    navController: NavController,
    settingsModel: SettingsViewModel
) {
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current
    val notesViewModel: NoteViewModel = viewModel()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        topBar = {
            key(settingsModel.darkTheme,settingsModel.dynamicTheme,settingsModel.amoledTheme) {
                AppBarView(
                    titleText = "Settings",
                    onBackNavClicked = {
                        navController.navigateUp()
                    }
                )
            }
        }
    )
    {
        val notesCount = notesViewModel.getAllNotes.collectAsState(initial = listOf()).value.size
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp, it.calculateTopPadding(), 16.dp, 16.dp)
        ) {
            SettingSection(sectionName = "Display") {
                SettingsBox(
                    title = "Dark Theme",
                    variable = settingsModel.darkTheme,
                    radius = arrayOf(16.dp, 16.dp, 0.dp, 0.dp)
                ) {
                    settingsModel.darkTheme = settingsModel.updateSetting("DARK_THEME",settingsModel.darkTheme)
                }
                SettingsBox(
                    title = "Dynamic Colors",
                    variable = settingsModel.dynamicTheme,
                    radius = arrayOf(0.dp, 0.dp, 0.dp, 0.dp)
                ) {
                    settingsModel.dynamicTheme = settingsModel.updateSetting("DYNAMIC_THEME",settingsModel.dynamicTheme)
                }
                SettingsBox(
                    title = "Amoled Colors",
                    variable = settingsModel.amoledTheme,
                    radius = arrayOf(0.dp, 0.dp, 16.dp, 16.dp)
                ) {
                    settingsModel.amoledTheme = settingsModel.updateSetting("AMOLED_THEME",settingsModel.amoledTheme)
                }
            }

            SettingSection(sectionName = "Database") {
                SettingsBox(
                    title = "Notes",
                    radius = arrayOf(16.dp, 16.dp, 16.dp, 16.dp),
                    customAction = {
                        Text(text = notesCount.toString())
                    }
                )
                Spacer(modifier = Modifier.height(32.dp))
                SettingsBox(
                    title = "Backup",
                    radius = arrayOf(16.dp, 16.dp, 0.dp, 0.dp),
                    customAction = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                            contentDescription = "Backup"
                        )
                    }
                ) {
                    Toast.makeText(context, "Not Supported yet", Toast.LENGTH_SHORT).show()
                }
                SettingsBox(
                    title = "Restore",
                    radius = arrayOf(0.dp, 0.dp, 16.dp, 16.dp),
                    customAction = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                            contentDescription = "Restore"
                        )
                    })
                {
                    Toast.makeText(context, "Not Supported yet", Toast.LENGTH_SHORT).show()
                }
            }

            SettingSection(sectionName = "About") {
                SettingsBox(
                    title = "Version",
                    radius = arrayOf(16.dp, 16.dp, 0.dp, 0.dp),
                    customAction = {
                        Text(settingsModel.version)
                    }
                )
                SettingsBox(
                    title = "Latest Release",
                    radius = arrayOf(0.dp, 0.dp, 16.dp, 16.dp),
                    customAction = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                            contentDescription = "Latest Release"
                        )
                    }) {
                    uriHandler.openUri("https://github.com/Kin69/EasyNotes/releases")
                }
                Spacer(modifier = Modifier.height(32.dp))
                SettingsBox(
                    title = "Support Us",
                    radius = arrayOf(16.dp, 16.dp, 16.dp, 16.dp),
                    customAction = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                            contentDescription = "Support Us"
                        )
                    }) {
                    uriHandler.openUri("https://github.com/Kin69/EasyNotes")
                }
            }
        }
    }
}