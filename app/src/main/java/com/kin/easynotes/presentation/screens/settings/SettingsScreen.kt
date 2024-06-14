package com.kin.easynotes.presentation.screens.settings

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Message
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.Backup
import androidx.compose.material.icons.rounded.Colorize
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.SecurityUpdate
import androidx.compose.material.icons.rounded.SettingsBackupRestore
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kin.easynotes.R
import com.kin.easynotes.presentation.components.NavigationIcon
import com.kin.easynotes.presentation.components.NotesScaffold
import com.kin.easynotes.presentation.components.TitleText
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel
import com.kin.easynotes.presentation.screens.settings.widgets.ActionType
import com.kin.easynotes.presentation.screens.settings.widgets.SettingSection
import com.kin.easynotes.presentation.screens.settings.widgets.SettingsBox

@Composable
fun SettingsView(
    onBackNavClicked: () -> Unit,
    settingsModel: SettingsViewModel
) {
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    NotesScaffold(
        topBar = { TopBar(onBackNavClicked, settingsModel) },
        content = {
            val notesCount = settingsModel.noteUseCase.getAllNotes.collectAsState(initial = listOf()).value.size
            SettingsContent(uriHandler, context, settingsModel, notesCount)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(onBackNavClicked: () -> Unit, settingsModel: SettingsViewModel) {
    key(settingsModel.settings.value) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
            title = { TitleText(titleText = stringResource(R.string.screen_settings)) },
            navigationIcon = { NavigationIcon { onBackNavClicked() } }
        )
    }
}

@Composable
fun SettingsContent(
    uriHandler: UriHandler,
    context: Context,
    settingsModel: SettingsViewModel,
    notesCount: Int
) {
    LazyColumn(modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 16.dp)) {
        item { DisplaySettings(settingsModel) }
        item { DatabaseSettings(context, notesCount) }
        item { AboutSettings(uriHandler, settingsModel) }
    }
}

@Composable
fun DisplaySettings(settingsModel: SettingsViewModel) {
    SettingSection(sectionName = stringResource(R.string.display)) {
        SettingsBox(
            title = stringResource(R.string.dark_theme),
            icon = Icons.Rounded.Palette,
            actionType = ActionType.SWITCH,
            variable = settingsModel.settings.value.darkTheme,
            shape = RoundedCornerShape(16.dp,16.dp,0.dp,0.dp),
            switchEnabled = { value -> settingsModel.update(settingsModel.settings.value.copy(automaticTheme = false, darkTheme = value)) }
        )
        SettingsBox(
            title = stringResource(R.string.dynamic_colors),
            icon = Icons.Rounded.Colorize,
            actionType = ActionType.SWITCH,
            variable = settingsModel.settings.value.dynamicTheme,
            switchEnabled = { value -> settingsModel.update(settingsModel.settings.value.copy(automaticTheme = false, dynamicTheme = value)) }
        )
        SettingsBox(
            title = stringResource(R.string.amoled_colors),
            icon = Icons.Rounded.DarkMode,
            actionType = ActionType.SWITCH,
            variable = settingsModel.settings.value.amoledTheme,
            shape = RoundedCornerShape(0.dp,0.dp,16.dp,16.dp),
            switchEnabled = { value -> settingsModel.update(settingsModel.settings.value.copy(amoledTheme = value)) }
        )
    }
}


@Composable
fun DatabaseSettings(context: Context, notesCount: Int) {
    val notSupported = stringResource(R.string.not_supported)

    SettingSection(sectionName = stringResource(R.string.database)) {
        SettingsBox(
            title = stringResource(R.string.notes),
            icon = Icons.AutoMirrored.Rounded.Message,
            shape = RoundedCornerShape(16.dp,16.dp,16.dp,16.dp),
            actionType = ActionType.TEXT,
            customText = notesCount.toString()
        )
        Spacer(modifier = Modifier.height(16.dp))
        SettingsBox(
            title = stringResource(id = R.string.backup),
            icon = Icons.Rounded.Backup,
            shape = RoundedCornerShape(16.dp,16.dp,0.dp,0.dp),
            actionType = ActionType.LINK,
            linkClicked = { Toast.makeText(context, notSupported, Toast.LENGTH_SHORT).show() }
        )
        SettingsBox(
            title = stringResource(R.string.restore),
            icon = Icons.Rounded.SettingsBackupRestore,
            shape = RoundedCornerShape(0.dp,0.dp,16.dp,16.dp),
            actionType = ActionType.LINK,
            linkClicked = { Toast.makeText(context, notSupported, Toast.LENGTH_SHORT).show() }
        )
    }
}

@Composable
fun AboutSettings(uriHandler: UriHandler, settingsModel: SettingsViewModel) {
    SettingSection(sectionName = stringResource(R.string.about)) {
        SettingsBox(
            title = stringResource(R.string.version),
            icon = Icons.Rounded.Info,
            shape = RoundedCornerShape(16.dp,16.dp,0.dp,0.dp),
            actionType = ActionType.TEXT,
            customText = settingsModel.version
        )
        SettingsBox(
            title = stringResource(R.string.latest_release),
            icon = Icons.Rounded.SecurityUpdate,
            shape = RoundedCornerShape(0.dp,0.dp,16.dp,16.dp),
            actionType = ActionType.LINK,
            linkClicked = { uriHandler.openUri("https://github.com/Kin69/EasyNotes/releases") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        SettingsBox(
            title = stringResource(R.string.start_project),
            icon = Icons.Rounded.Star,
            shape = RoundedCornerShape(16.dp,16.dp,0.dp,0.dp),
            actionType = ActionType.LINK,
            linkClicked = { uriHandler.openUri("https://github.com/Kin69/EasyNotes") }
        )
        SettingsBox(
            title = stringResource(R.string.donate_me),
            icon = Icons.Rounded.AttachMoney,
            shape = RoundedCornerShape(0.dp,0.dp,16.dp,16.dp),
            actionType = ActionType.LINK,
            linkClicked = { uriHandler.openUri("https://ko-fi.com/kin69_") }
        )
    }
}
