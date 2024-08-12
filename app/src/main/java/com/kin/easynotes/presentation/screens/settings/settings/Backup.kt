package com.kin.easynotes.presentation.screens.settings.settings

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Backup
import androidx.compose.material.icons.rounded.EnhancedEncryption
import androidx.compose.material.icons.rounded.FileOpen
import androidx.compose.material.icons.rounded.ImportExport
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.kin.easynotes.R
import com.kin.easynotes.core.constant.DatabaseConst
import com.kin.easynotes.presentation.screens.edit.components.CustomTextField
import com.kin.easynotes.presentation.screens.settings.SettingsScaffold
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel
import com.kin.easynotes.presentation.screens.settings.widgets.ActionType
import com.kin.easynotes.presentation.screens.settings.widgets.SettingsBox
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun CloudScreen(navController: NavController, settingsViewModel: SettingsViewModel) {
    val context = LocalContext.current

    val exportBackupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("*/.zip"),
        onResult = { uri ->
            if (uri != null) settingsViewModel.onExportBackup(uri, context)
        }
    )
    val importBackupLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.OpenDocument(),
    onResult = { uri ->
        if (uri != null) settingsViewModel.onImportBackup(uri, context)
        }
    )

    val importFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments(),
        onResult = { uris ->
            settingsViewModel.onImportFiles(uris, context)
        }
    )

    SettingsScaffold(
        settingsViewModel = settingsViewModel,
        title = stringResource(id = R.string.backup),
        onBackNavClicked = { navController.navigateUp() }
    ) {
        LazyColumn {
            item {
                SettingsBox(
                    title = stringResource(id = R.string.encrypt_databse),
                    description = stringResource(id = R.string.encrypt_databse_description),
                    icon = Icons.Rounded.EnhancedEncryption,
                    radius = shapeManager(radius = settingsViewModel.settings.value.cornerRadius, isBoth = true),
                    variable = settingsViewModel.settings.value.encryptBackup,
                    actionType = ActionType.SWITCH,
                    switchEnabled = { settingsViewModel.update(settingsViewModel.settings.value.copy(encryptBackup = it)) }
                )
                Spacer(modifier = Modifier.height(18.dp))
            }
            item {
                SettingsBox(
                    title = stringResource(id = R.string.backup),
                    description = stringResource(id = R.string.backup_description),
                    icon = Icons.Rounded.Backup,
                    radius = shapeManager(radius = settingsViewModel.settings.value.cornerRadius, isFirst = true),
                    actionType = ActionType.CUSTOM,
                    customAction = {onExit ->
                        if (settingsViewModel.settings.value.encryptBackup) {
                            PasswordPrompt(
                                context = context,
                                text = stringResource(id = R.string.backup),
                                settingsViewModel = settingsViewModel,
                                onExit = { password ->
                                    if (password != null) {
                                        settingsViewModel.password = password.text
                                    }
                                    onExit()
                                },
                                onBackup = {
                                    exportBackupLauncher.launch("${DatabaseConst.NOTES_DATABASE_BACKUP_NAME}-${currentDateTime()}.zip")
                                }
                            )
                        }
                        else {
                            LaunchedEffect(true) {
                                exportBackupLauncher.launch("${DatabaseConst.NOTES_DATABASE_BACKUP_NAME}-${currentDateTime()}.zip")
                            }
                        }
                    }
                )
            }
            item {
                SettingsBox(
                    title = stringResource(id = R.string.restore),
                    description = stringResource(id = R.string.restore_description),
                    icon = Icons.Rounded.ImportExport,
                    radius = shapeManager(radius = settingsViewModel.settings.value.cornerRadius, isLast = true),
                    actionType = ActionType.CUSTOM,
                    customAction = { onExit ->
                        if (settingsViewModel.settings.value.encryptBackup) {
                            PasswordPrompt(
                                context = context,
                                text = stringResource(id = R.string.restore),
                                settingsViewModel = settingsViewModel,
                                onExit = { password ->
                                    if (password != null) {
                                        settingsViewModel.password = password.text
                                    }
                                    onExit()
                                },
                                onBackup = {
                                    importBackupLauncher.launch(arrayOf("application/zip"))
                                }
                            )
                        }
                        else {
                            LaunchedEffect(true) {
                                settingsViewModel.password = null
                                importBackupLauncher.launch(arrayOf("application/zip"))
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(18.dp))
            }
            item {
                SettingsBox(
                    title = context.getString(R.string.file_import_title),
                    description = context.getString(R.string.file_import_description),
                    icon = Icons.Rounded.FileOpen,
                    radius = shapeManager(radius = settingsViewModel.settings.value.cornerRadius, isBoth = true),
                    actionType = ActionType.CUSTOM,
                    customAction = {
                        LaunchedEffect(true) {
                            importFileLauncher.launch(arrayOf("text/*"))
                        }
                    }
                )
            }
        }
    }
}

fun currentDateTime(): String {
    val currentDateTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("MM-dd-HH-mm-ms")
    val formattedDateTime = currentDateTime.format(formatter)

    return formattedDateTime
}

@Composable
fun PasswordPrompt(context: Context, text: String, settingsViewModel: SettingsViewModel, onExit: (TextFieldValue?) -> Unit, onBackup: () -> Unit = {}) {
    var password by remember { mutableStateOf(TextFieldValue("")) }
    Dialog(
        onDismissRequest = { onExit(null) },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        LazyColumn {
            item {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .fillMaxHeight(0.2f)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceContainerLow,
                            shape = shapeManager(
                                isBoth = true,
                                radius = settingsViewModel.settings.value.cornerRadius
                            )
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .padding(12.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surfaceContainerHighest,
                                shape = shapeManager(
                                    isBoth = true,
                                    radius = settingsViewModel.settings.value.cornerRadius
                                )
                            )
                    ) {
                        CustomTextField(
                            hideContent = true,
                            value = password,
                            onValueChange = { password = it },
                            placeholder = stringResource(id = R.string.password_prompt)
                        )
                    }
                    Button(
                        modifier = Modifier
                            .padding(12.dp)
                            .wrapContentWidth()
                            .align(Alignment.End),
                        onClick = {
                            if (password.text.isNotBlank()) {
                                onExit(password)
                                onBackup()
                            } else {
                                Toast.makeText(context, R.string.invalid_input, Toast.LENGTH_SHORT).show()
                            }
                        },
                        content = {
                            Text(text)
                        }
                    )
                }
            }
        }
    }
}

