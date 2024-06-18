package com.kin.easynotes.presentation.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.Cloud
import androidx.compose.material.icons.rounded.Colorize
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.HdrAuto
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.TextFields
import androidx.compose.material.icons.rounded.Widgets
import androidx.compose.material.icons.rounded.Work
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kin.easynotes.R
import com.kin.easynotes.presentation.components.NavigationIcon
import com.kin.easynotes.presentation.components.NotesScaffold
import com.kin.easynotes.presentation.components.TitleText
import com.kin.easynotes.presentation.navigation.NavRoutes
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel
import com.kin.easynotes.presentation.screens.settings.widgets.ActionType

import com.kin.easynotes.presentation.screens.settings.widgets.SettingCategory
import com.kin.easynotes.presentation.screens.settings.widgets.SettingsBox
import com.kin.easynotes.presentation.screens.settings.widgets.SmallSettingCategory

@Composable
fun SettingsScaffold(
    title: String,
    onBackNavClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    NotesScaffold(
        topBar = { TopBar(title, onBackNavClicked, ) },
        content = {
            Box(Modifier.padding(16.dp, 8.dp, 16.dp, 16.dp)) {
                content()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    onBackNavClicked: () -> Unit,
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        title = {
            TitleText(titleText = title)
        },
        navigationIcon = { NavigationIcon { onBackNavClicked() } }
    )
}

@Composable
fun MainSettings(navController: NavController) {
    SettingsScaffold(
        title = stringResource(id = R.string.screen_settings),
        onBackNavClicked = { navController.navigate(NavRoutes.Home.route) }
    ) {
        val uriHandler = LocalUriHandler.current

        LazyColumn {
            item {
                SmallSettingCategory(
                    title = stringResource(R.string.support),
                    subTitle = stringResource(id = R.string.support_description),
                    icon = Icons.Rounded.ArrowForwardIos,
                    shape = RoundedCornerShape(64.dp),
                    action = { uriHandler.openUri("https://ko-fi.com/kin69_") })
            }
            item {
                SettingCategory(
                    title = stringResource(id = R.string.color_styles),
                    subTitle = stringResource(R.string.description_color_styles),
                    icon = Icons.Rounded.Palette,
                    shape = RoundedCornerShape(32.dp, 32.dp, 6.dp, 6.dp),
                    action = { navController.navigate(NavRoutes.ColorStyles.route) })
            }
            item {
                SettingCategory(
                    title = stringResource(id = R.string.language),
                    subTitle = stringResource(R.string.description_language),
                    icon = Icons.Rounded.Language,
                    shape = RoundedCornerShape(6.dp, 6.dp, 32.dp, 32.dp),
                    isLast = true,
                    action = { navController.navigate(NavRoutes.Language.route) })
            }
            item {
                SettingCategory(
                    title = stringResource(id = R.string.cloud),
                    subTitle = stringResource(R.string.description_cloud),
                    icon = Icons.Rounded.Cloud,
                    shape = RoundedCornerShape(32.dp, 32.dp, 6.dp, 6.dp),
                    action = { navController.navigate(NavRoutes.Cloud.route) })
            }
            item {
                SettingCategory(
                    title = stringResource(id = R.string.markdown),
                    subTitle = stringResource(id = R.string.description_markdown),
                    icon = Icons.Rounded.TextFields,
                    shape = RoundedCornerShape(6.dp, 6.dp, 6.dp, 6.dp),
                    action = { navController.navigate(NavRoutes.Markdown.route) })
            }
            item {
                SettingCategory(
                    title = stringResource(id = R.string.tools),
                    subTitle = stringResource(R.string.description_tools),
                    icon = Icons.Rounded.Work,
                    shape = RoundedCornerShape(6.dp, 6.dp, 32.dp, 32.dp),
                    isLast = true,
                    action = { navController.navigate(NavRoutes.Tools.route) })
            }
            item {
                SettingCategory(
                    title = stringResource(id = R.string.history),
                    subTitle = stringResource(R.string.description_history),
                    icon = Icons.Rounded.History,
                    shape = RoundedCornerShape(32.dp, 32.dp, 6.dp, 6.dp),
                    action = { navController.navigate(NavRoutes.History.route) })
            }
            item {
                SettingCategory(
                    title = stringResource(id = R.string.widgets),
                    subTitle = stringResource(R.string.description_widgets),
                    icon = Icons.Rounded.Widgets,
                    shape = RoundedCornerShape(6.dp, 6.dp, 32.dp, 32.dp),
                    isLast = true,
                    action = { navController.navigate(NavRoutes.Widgets.route) })
            }
            item {
                SettingCategory(
                    title = stringResource(id = R.string.about),
                    subTitle = stringResource(R.string.description_about),
                    icon = Icons.Rounded.Info,
                    shape = RoundedCornerShape(32.dp, 32.dp, 32.dp, 32.dp),
                    action = { navController.navigate(NavRoutes.About.route) })
            }
        }
    }
}

@Composable
fun ColorStylesScreen(navController: NavController, settingsViewModel: SettingsViewModel) {
    key(settingsViewModel.settings.value) {
        SettingsScaffold(
            title = stringResource(id = R.string.color_styles),
            onBackNavClicked = { navController.popBackStack() }
        ) {
            LazyColumn (
                modifier = Modifier.clip(RoundedCornerShape(32.dp))
            ) {
                item {
                    SettingsBox(
                        title = stringResource(id = R.string.system_theme),
                        icon = Icons.Rounded.HdrAuto,
                        actionType = ActionType.SWITCH,
                        variable = settingsViewModel.settings.value.automaticTheme,
                        switchEnabled = { settingsViewModel.update(settingsViewModel.settings.value.copy(automaticTheme = it))}
                    )
                }

                if (!settingsViewModel.settings.value.automaticTheme) {
                    item {
                        SettingsBox(
                            title = stringResource(id = R.string.dark_theme),
                            icon = Icons.Rounded.DarkMode,
                            actionType = ActionType.SWITCH,
                            variable = settingsViewModel.settings.value.darkTheme,
                            switchEnabled = { settingsViewModel.update(settingsViewModel.settings.value.copy(automaticTheme = false, darkTheme = it))}
                        )
                    }

                    item {
                        SettingsBox(
                            title = stringResource(id = R.string.dynamic_colors),
                            icon = Icons.Rounded.Colorize,
                            actionType = ActionType.SWITCH,
                            variable = settingsViewModel.settings.value.dynamicTheme,
                            switchEnabled = { settingsViewModel.update(settingsViewModel.settings.value.copy(automaticTheme = false, dynamicTheme = it))}
                        )
                    }
                }

                if (settingsViewModel.settings.value.darkTheme) {
                    item {
                        SettingsBox(
                            title = stringResource(id = R.string.amoled_colors),
                            icon = Icons.Rounded.Palette,
                            actionType = ActionType.SWITCH,
                            variable = settingsViewModel.settings.value.amoledTheme,
                            switchEnabled = { settingsViewModel.update(settingsViewModel.settings.value.copy(amoledTheme = it))}
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LanguageScreen(navController: NavController, settingsViewModel: SettingsViewModel) {
    SettingsScaffold(
        title = stringResource(id = R.string.language),
        onBackNavClicked = { navController.popBackStack() }
    ) {

    }

}

@Composable
fun CloudScreen(navController: NavController, settingsViewModel: SettingsViewModel) {
    SettingsScaffold(
        title = stringResource(id = R.string.cloud),
        onBackNavClicked = { navController.popBackStack() }
    ) {

    }

}

@Composable
fun MarkdownScreen(navController: NavController, settingsViewModel: SettingsViewModel) {
    SettingsScaffold(
        title = stringResource(id = R.string.markdown),
        onBackNavClicked = { navController.popBackStack() }
    ) {

    }

}

@Composable
fun ToolsScreen(navController: NavController, settingsViewModel: SettingsViewModel) {
    SettingsScaffold(
        title = stringResource(id = R.string.tools),
        onBackNavClicked = { navController.popBackStack() }
    ) {

    }

}

@Composable
fun HistoryScreen(navController: NavController, settingsViewModel: SettingsViewModel) {
    SettingsScaffold(
        title = stringResource(id = R.string.history),
        onBackNavClicked = { navController.popBackStack() }
    ) {

    }

}

@Composable
fun WidgetsScreen(navController: NavController, settingsViewModel: SettingsViewModel) {
    SettingsScaffold(
        title = stringResource(id = R.string.widgets),
        onBackNavClicked = { navController.popBackStack() }
    ) {

    }

}

@Composable
fun AboutScreen(navController: NavController, settingsViewModel: SettingsViewModel) {
    SettingsScaffold(
        title = stringResource(id = R.string.about),
        onBackNavClicked = { navController.popBackStack() }
    ) {

    }

}