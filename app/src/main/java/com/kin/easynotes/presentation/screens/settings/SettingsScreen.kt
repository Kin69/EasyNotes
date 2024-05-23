package com.kin.easynotes.presentation.screens.settings

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.rounded.Message
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material.icons.rounded.Backup
import androidx.compose.material.icons.rounded.Colorize
import androidx.compose.material.icons.rounded.Copyright
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.LocalCafe
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.SecurityUpdate
import androidx.compose.material.icons.rounded.SettingsBackupRestore
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kin.easynotes.presentation.components.NavigationIcon
import com.kin.easynotes.presentation.components.NotesScaffold
import com.kin.easynotes.presentation.components.TitleText
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel
import com.kin.easynotes.presentation.screens.settings.widgets.SettingSection
import com.kin.easynotes.presentation.screens.settings.widgets.SettingsBox
import com.kin.easynotes.presentation.theme.GlobalFont


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsView(
    navController: NavController,
    settingsModel: SettingsViewModel
) {
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    NotesScaffold(
        topBar = {
            key(settingsModel.darkTheme,settingsModel.dynamicTheme,settingsModel.amoledTheme) {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
                    title = { TitleText(titleText = "Settings") },
                    navigationIcon = { NavigationIcon() { navController.navigateUp() } }
                )
            }
        },
        content = {
            val notesCount = settingsModel.getAllNotes.collectAsState(initial = listOf()).value.size
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp, 0.dp,16.dp,16.dp)
            ) {
                SettingSection(sectionName = "Display") {
                    SettingsBox(
                        title = "Dark Theme",
                        icon = Icons.Rounded.Palette,
                        variable = settingsModel.darkTheme,
                        radius = arrayOf(16.dp, 16.dp, 0.dp, 0.dp)
                    ) {
                        settingsModel.darkTheme = settingsModel.updateSetting("DARK_THEME",settingsModel.darkTheme)
                    }
                    SettingsBox(
                        title = "Dynamic Colors",
                        icon = Icons.Rounded.Colorize,
                        variable = settingsModel.dynamicTheme,
                        radius = arrayOf(0.dp, 0.dp, 0.dp, 0.dp)
                    ) {
                        settingsModel.dynamicTheme = settingsModel.updateSetting("DYNAMIC_THEME",settingsModel.dynamicTheme)
                    }
                    SettingsBox(
                        title = "Amoled Colors",
                        icon = Icons.Rounded.DarkMode,
                        variable = settingsModel.amoledTheme,
                        radius = arrayOf(0.dp, 0.dp, 16.dp, 16.dp)
                    ) {
                        settingsModel.amoledTheme = settingsModel.updateSetting("AMOLED_THEME",settingsModel.amoledTheme)
                    }
                }

                SettingSection(sectionName = "Database") {
                    SettingsBox(
                        title = "Notes",
                        icon = Icons.AutoMirrored.Rounded.Message,
                        radius = arrayOf(16.dp, 16.dp, 16.dp, 16.dp),
                        customAction = {
                            Text(text = notesCount.toString(),fontFamily = GlobalFont)
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    SettingsBox(
                        title = "Backup",
                        icon = Icons.Rounded.Backup,
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
                        icon = Icons.Rounded.SettingsBackupRestore,
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
                        icon = Icons.Rounded.Info,
                        radius = arrayOf(16.dp, 16.dp, 0.dp, 0.dp),
                        customAction = {
                            Text(settingsModel.version)
                        }
                    )
                    SettingsBox(
                        title = "License",
                        icon = Icons.Rounded.Copyright,
                        radius = arrayOf(0.dp, 0.dp, 0.dp, 0.dp),
                        customAction = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.OpenInNew,
                                contentDescription = "Latest Release"
                            )
                        }) {
                        uriHandler.openUri("https://github.com/Kin69/EasyNotes/blob/master/LICENSE")
                    }
                    SettingsBox(
                        title = "Latest Release",
                        icon = Icons.Rounded.SecurityUpdate,
                        radius = arrayOf(0.dp, 0.dp, 16.dp, 16.dp),
                        customAction = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.OpenInNew,
                                contentDescription = "Latest Release"
                            )
                        }) {
                        uriHandler.openUri("https://github.com/Kin69/EasyNotes/releases")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    SettingsBox(
                        title = "Support Us",
                        icon = Icons.Rounded.LocalCafe,
                        radius = arrayOf(16.dp, 16.dp, 16.dp, 16.dp),
                        customAction = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.OpenInNew,
                                contentDescription = "Support Us"
                            )
                        }) {
                        uriHandler.openUri("https://github.com/Kin69/EasyNotes")
                    }
                }
            }
        }
    )
}