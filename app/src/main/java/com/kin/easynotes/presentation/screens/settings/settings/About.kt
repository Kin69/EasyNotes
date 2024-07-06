package com.kin.easynotes.presentation.screens.settings.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BugReport
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.Coffee
import androidx.compose.material.icons.rounded.ContactSupport
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.kin.easynotes.R
import com.kin.easynotes.core.constant.ConnectionConst
import com.kin.easynotes.core.constant.SupportConst
import com.kin.easynotes.presentation.screens.settings.SettingsScaffold
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel
import com.kin.easynotes.presentation.screens.settings.widgets.ActionType
import com.kin.easynotes.presentation.screens.settings.widgets.SettingsBox


@Composable
fun AboutScreen(navController: NavController, settingsViewModel: SettingsViewModel) {
    val uriHandler = LocalUriHandler.current
    SettingsScaffold(
        settingsViewModel = settingsViewModel,
        title = stringResource(id = R.string.about),
        onBackNavClicked = { navController.navigateUp() }
    ) {
        LazyColumn {
            item {
                SettingsBox(
                    title = stringResource(id = R.string.support_list),
                    icon = Icons.Rounded.Coffee,
                    actionType = ActionType.CUSTOM,
                    radius = shapeManager(isBoth = true, radius = settingsViewModel.settings.value.cornerRadius),
                    customAction = { onExit -> ListDialog(list = SupportConst.SUPPROTERS_LIST, settingsViewModel = settingsViewModel) { onExit() } }
                )
                Spacer(modifier = Modifier.height(18.dp))
            }
            item {
                SettingsBox(
                    title = stringResource(id = R.string.build_type),
                    description = settingsViewModel.build,
                    icon = Icons.Rounded.Build,
                    actionType = ActionType.TEXT,
                    radius = shapeManager(isFirst = true, radius = settingsViewModel.settings.value.cornerRadius)
                )
            }
            item {
                SettingsBox(
                    title = stringResource(id = R.string.version),
                    description = settingsViewModel.version,
                    icon = Icons.Rounded.Info,
                    actionType = ActionType.TEXT,
                    radius = shapeManager(isLast = true, radius = settingsViewModel.settings.value.cornerRadius),
                )
                Spacer(modifier = Modifier.height(18.dp))
            }
            item {
                SettingsBox(
                    title = stringResource(id = R.string.latest_release),
                    icon = Icons.Rounded.Verified,
                    actionType = ActionType.LINK,
                    radius = shapeManager(isFirst = true, radius = settingsViewModel.settings.value.cornerRadius),
                    linkClicked = { uriHandler.openUri("https://github.com/Kin69/EasyNotes/releases") }
                )
            }
            item {
                SettingsBox(
                    title = stringResource(id = R.string.source_code),
                    icon = Icons.Rounded.Download,
                    actionType = ActionType.LINK,
                    radius = shapeManager(isLast = true, radius = settingsViewModel.settings.value.cornerRadius),
                    linkClicked = { uriHandler.openUri("https://github.com/Kin69/EasyNotes/releases") }
                )
                Spacer(modifier = Modifier.height(18.dp))
            }

            item {
                SettingsBox(
                    title = stringResource(id = R.string.email),
                    icon = Icons.Rounded.Email,
                    clipboardText = ConnectionConst.SUPPORT_MAIL,
                    actionType = ActionType.CLIPBOARD,
                    radius = shapeManager(isFirst = true, radius = settingsViewModel.settings.value.cornerRadius),
                )
            }
            item {
                SettingsBox(
                    isBig = true,
                    title = stringResource(id = R.string.discord),
                    icon = Icons.Rounded.ContactSupport,
                    actionType = ActionType.LINK,
                    linkClicked = { uriHandler.openUri(ConnectionConst.SUPPORT_DISCORD) },
                    radius = shapeManager(radius = settingsViewModel.settings.value.cornerRadius),
                )
            }
            item {
                SettingsBox(
                    isBig = true,
                    title = stringResource(id = R.string.feature),
                    icon = Icons.Rounded.BugReport,
                    linkClicked = { uriHandler.openUri(ConnectionConst.GITHUB_FEATURE_REQUEST) },
                    actionType = ActionType.LINK,
                    radius = shapeManager(isLast = true, radius = settingsViewModel.settings.value.cornerRadius),
                )
            }
        }
    }

}

@Composable
fun ListDialog( list: List<Pair<String,String>>, settingsViewModel: SettingsViewModel, onExit: () -> Unit) {
    Dialog(
        onDismissRequest = { onExit() },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        LazyColumn(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    shape = shapeManager(isBoth = true, radius = settingsViewModel.settings.value.cornerRadius)
                )
                .padding(14.dp)
                .fillMaxSize(.8f)

        ) {
            itemsIndexed(list) { index, (name, githubUrl) ->
                val isFirstItem = index == 0
                val isLastItem = index == list.lastIndex

                SettingsBox(
                    title = name,
                    description = githubUrl,
                    radius = shapeManager(
                        isFirst = isFirstItem,
                        isLast = isLastItem,
                        radius = settingsViewModel.settings.value.cornerRadius
                    ),
                    actionType = ActionType.TEXT,
                    customText = "❤"
                )
            }
        }

    }
}

