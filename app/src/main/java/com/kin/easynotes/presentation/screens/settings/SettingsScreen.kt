package com.kin.easynotes.presentation.screens.settings

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.rounded.Message
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.Backup
import androidx.compose.material.icons.rounded.Colorize
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.SecurityUpdate
import androidx.compose.material.icons.rounded.SettingsBackupRestore
import androidx.compose.material.icons.rounded.Star
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kin.easynotes.R
import com.kin.easynotes.presentation.components.NavigationIcon
import com.kin.easynotes.presentation.components.NotesScaffold
import com.kin.easynotes.presentation.components.TitleText
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel
import com.kin.easynotes.presentation.screens.settings.widgets.SettingSection
import com.kin.easynotes.presentation.screens.settings.widgets.SettingsBox

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
                    title = { TitleText(titleText = stringResource(R.string.screen_settings)) },
                    navigationIcon = { NavigationIcon() { navController.navigateUp() } }
                )
            }
        },
        content = {
            val notesCount = settingsModel.noteUseCase.getAllNotes.collectAsState(initial = listOf()).value.size
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp, 0.dp,16.dp,16.dp)
            ) {
                item {
                    SettingSection(sectionName = stringResource(R.string.display)) {
                        SettingsBox(
                            title = stringResource(R.string.dark_theme),
                            icon = Icons.Rounded.Palette,
                            variable = settingsModel.darkTheme,
                            radius = arrayOf(16.dp, 16.dp, 0.dp, 0.dp)
                        ) {
                            settingsModel.darkTheme = settingsModel.updateSetting("dark_theme",settingsModel.darkTheme)
                        }
                        SettingsBox(
                            title = stringResource(R.string.dynamic_colors),
                            icon = Icons.Rounded.Colorize,
                            variable = settingsModel.dynamicTheme,
                            radius = arrayOf(0.dp, 0.dp, 0.dp, 0.dp)
                        ) {
                            settingsModel.dynamicTheme = settingsModel.updateSetting("dynamic_theme",settingsModel.dynamicTheme)
                        }
                        SettingsBox(
                            title = stringResource(R.string.amoled_colors),
                            icon = Icons.Rounded.DarkMode,
                            variable = settingsModel.amoledTheme,
                            radius = arrayOf(0.dp, 0.dp, 16.dp, 16.dp)
                        ) {
                            settingsModel.amoledTheme = settingsModel.updateSetting("amoled_theme",settingsModel.amoledTheme)
                        }
                    }
                }
                item {
                    SettingSection(sectionName = stringResource(R.string.database)) {
                        SettingsBox(
                            title = "Notes",
                            icon = Icons.AutoMirrored.Rounded.Message,
                            radius = arrayOf(16.dp, 16.dp, 16.dp, 16.dp),
                            customAction = {
                                Text(text = notesCount.toString())
                            }
                        )
                        val not_supported = stringResource(R.string.not_supported)
                        Spacer(modifier = Modifier.height(16.dp))
                        SettingsBox(
                            title = "Backup",
                            icon = Icons.Rounded.Backup,
                            radius = arrayOf(16.dp, 16.dp, 0.dp, 0.dp),
                            customAction = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                    contentDescription = stringResource(R.string.backup)
                                )
                            }
                        ) {
                            Toast.makeText(context, not_supported, Toast.LENGTH_SHORT).show()
                        }
                        SettingsBox(
                            title = "Restore",
                            icon = Icons.Rounded.SettingsBackupRestore,
                            radius = arrayOf(0.dp, 0.dp, 16.dp, 16.dp),
                            customAction = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                    contentDescription = null
                                )
                            })
                        {

                            Toast.makeText(context, not_supported, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                item {
                    SettingSection(sectionName = stringResource(R.string.about)) {
                        SettingsBox(
                            title = stringResource(R.string.version),
                            icon = Icons.Rounded.Info,
                            radius = arrayOf(16.dp, 16.dp, 0.dp, 0.dp),
                            customAction = {
                                Text(settingsModel.version)
                            }
                        )
                        SettingsBox(
                            title = stringResource(R.string.latest_release),
                            icon = Icons.Rounded.SecurityUpdate,
                            radius = arrayOf(0.dp, 0.dp, 16.dp, 16.dp),
                            customAction = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.OpenInNew,
                                    contentDescription = null
                                )
                            }) {
                            uriHandler.openUri("https://github.com/Kin69/EasyNotes/releases")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        SettingsBox(
                            title = stringResource(R.string.start_project),
                            icon = Icons.Rounded.Star,
                            radius = arrayOf(16.dp, 16.dp, 0.dp, 0.dp),
                            customAction = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.OpenInNew,
                                    contentDescription = null
                                )
                            }) {
                            uriHandler.openUri("https://github.com/Kin69/EasyNotes")
                        }
                        SettingsBox(
                            title = stringResource(R.string.donate_me),
                            icon = Icons.Rounded.AttachMoney,
                            radius = arrayOf(0.dp, 0.dp, 16.dp, 16.dp),
                            customAction = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.OpenInNew,
                                    contentDescription = null
                                )
                            }) {
                            uriHandler.openUri("https://ko-fi.com/kin69_")
                        }
                    }
                }
            }
        }
    )
}
