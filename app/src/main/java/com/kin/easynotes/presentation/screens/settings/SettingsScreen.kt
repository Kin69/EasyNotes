package com.kin.easynotes.presentation.screens.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.Cloud
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.TextFields
import androidx.compose.material.icons.rounded.Widgets
import androidx.compose.material.icons.rounded.Work
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
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
import com.kin.easynotes.presentation.screens.settings.settings.shapeManager
import com.kin.easynotes.presentation.screens.settings.widgets.SettingCategory
import com.kin.easynotes.presentation.screens.settings.widgets.SmallSettingCategory

@Composable
fun SettingsScaffold(
    settingsViewModel: SettingsViewModel,
    title: String,
    onBackNavClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    NotesScaffold(
        topBar = {
            key(settingsViewModel.settings.value) {
                TopBar(title, onBackNavClicked, )
            }
        },
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
fun MainSettings(settingsViewModel: SettingsViewModel,navController: NavController) {
    SettingsScaffold(
        settingsViewModel = settingsViewModel,
        title = stringResource(id = R.string.screen_settings),
        onBackNavClicked = { navController.navigate(NavRoutes.Home.route) }
    ) {
        val uriHandler = LocalUriHandler.current

        LazyColumn {
            item {
                SmallSettingCategory(
                    title = stringResource(R.string.support),
                    subTitle = stringResource(id = R.string.support_description),
                    icon = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                    shape = shapeManager(radius = settingsViewModel.settings.value.cornerRadius, isBoth = true),
                    action = { uriHandler.openUri("https://ko-fi.com/kin69_") })
            }
            item {
                SettingCategory(
                    title = stringResource(id = R.string.color_styles),
                    subTitle = stringResource(R.string.description_color_styles),
                    icon = Icons.Rounded.Palette,
                    shape = shapeManager(radius = settingsViewModel.settings.value.cornerRadius, isFirst = true),
                    action = { navController.navigate(NavRoutes.ColorStyles.route) })
            }
//            item {
//                SettingCategory(
//                    title = stringResource(id = R.string.language),
//                    subTitle = stringResource(R.string.description_language),
//                    icon = Icons.Rounded.Language,
//                    shape = shapeManager(radius = settingsViewModel.settings.value.cornerRadius, isLast = true),
//                    isLast = true,
//                    action = { navController.navigate(NavRoutes.Language.route) })
//            }
//            item {
//                SettingCategory(
//                    title = stringResource(id = R.string.cloud),
//                    subTitle = stringResource(R.string.description_cloud),
//                    icon = Icons.Rounded.Cloud,
//                    shape = shapeManager(radius = settingsViewModel.settings.value.cornerRadius, isFirst = true),
//                    action = { navController.navigate(NavRoutes.Cloud.route) })
//            }
            item {
                SettingCategory(
                    title = stringResource(id = R.string.markdown),
                    isLast = true,
                    subTitle = stringResource(id = R.string.description_markdown),
                    icon = Icons.Rounded.TextFields,
                    shape = shapeManager(radius = settingsViewModel.settings.value.cornerRadius, isLast = true),
                    action = { navController.navigate(NavRoutes.Markdown.route) })
            }
//            item {
//                SettingCategory(
//                    title = stringResource(id = R.string.tools),
//                    subTitle = stringResource(R.string.description_tools),
//                    icon = Icons.Rounded.Work,
//                    shape = shapeManager(radius = settingsViewModel.settings.value.cornerRadius, isLast = true),
//                    isLast = true,
//                    action = { navController.navigate(NavRoutes.Tools.route) })
//            }
//            item {
//                SettingCategory(
//                    title = stringResource(id = R.string.history),
//                    subTitle = stringResource(R.string.description_history),
//                    icon = Icons.Rounded.History,
//                    shape = shapeManager(radius = settingsViewModel.settings.value.cornerRadius, isFirst = true),
//                    action = { navController.navigate(NavRoutes.History.route) })
//            }
//            item {
//                SettingCategory(
//                    title = stringResource(id = R.string.widgets),
//                    subTitle = stringResource(R.string.description_widgets),
//                    icon = Icons.Rounded.Widgets,
//                    shape = shapeManager(radius = settingsViewModel.settings.value.cornerRadius, isLast = true),
//                    isLast = true,
//                    action = { navController.navigate(NavRoutes.Widgets.route) })
//            }
            item {
                SettingCategory(
                    title = stringResource(id = R.string.about),
                    subTitle = stringResource(R.string.description_about),
                    icon = Icons.Rounded.Info,
                    shape = shapeManager(radius = settingsViewModel.settings.value.cornerRadius, isBoth = true),
                    action = { navController.navigate(NavRoutes.About.route) })
            }
        }
    }
}

@Composable
fun CloudScreen(navController: NavController, settingsViewModel: SettingsViewModel) {
    SettingsScaffold(
        settingsViewModel = settingsViewModel,
        title = stringResource(id = R.string.cloud),
        onBackNavClicked = { navController.popBackStack() }
    ) {

    }

}

@Composable
fun ToolsScreen(navController: NavController, settingsViewModel: SettingsViewModel) {
    SettingsScaffold(
        settingsViewModel = settingsViewModel,
        title = stringResource(id = R.string.tools),
        onBackNavClicked = { navController.popBackStack() }
    ) {

    }

}

@Composable
fun HistoryScreen(navController: NavController, settingsViewModel: SettingsViewModel) {
    SettingsScaffold(
        settingsViewModel = settingsViewModel,
        title = stringResource(id = R.string.history),
        onBackNavClicked = { navController.popBackStack() }
    ) {

    }

}

@Composable
fun WidgetsScreen(navController: NavController, settingsViewModel: SettingsViewModel) {
    SettingsScaffold(
        settingsViewModel = settingsViewModel,
        title = stringResource(id = R.string.widgets),
        onBackNavClicked = { navController.popBackStack() }
    ) {

    }

}
