package com.kin.easynotes.presentation.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cloud
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.TextFields
import androidx.compose.material.icons.rounded.Work
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kin.easynotes.R
import com.kin.easynotes.presentation.components.material.MaterialScaffold
import com.kin.easynotes.presentation.components.material.MaterialBar
import com.kin.easynotes.presentation.navigation.NavRoutes
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel
import com.kin.easynotes.presentation.screens.settings.widgets.SectionBlock
import com.kin.easynotes.presentation.screens.settings.widgets.SettingSection
import com.kin.easynotes.presentation.screens.settings.widgets.SupportBox

@Composable
fun SettingsScaffold(
    settingsViewModel: SettingsViewModel,
    title: String,
    onBackNavClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    MaterialScaffold(
        topBar = {
            key(settingsViewModel.settings.value) {
                MaterialBar(
                    title = title,
                    onBackNavClicked = onBackNavClicked
                )
            }
        },
        content = {
            Box(Modifier.padding(16.dp, 8.dp, 16.dp)) {
                content()
            }
        }
    )
}

@Composable
fun MainSettings(settingsViewModel: SettingsViewModel, navController: NavController) {
    var showSupportDialog by remember { mutableStateOf(false) }

    if (showSupportDialog) {
        SupportContent(
            navController = navController,
            settingsViewModel = settingsViewModel,
            onExit = { showSupportDialog = false }
        )
    }

    SettingsScaffold(
        settingsViewModel = settingsViewModel,
        title = stringResource(id = R.string.screen_settings),
        onBackNavClicked = { navController.navigateUp() }
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            item {
                SupportBox(
                    title = stringResource(id = R.string.support),
                    description = stringResource(id = R.string.support_description),
                    onAction = { showSupportDialog = true }
                )
            }
            item {
                SectionBlock(
                    listOf(
                        SettingSection(
                            title = stringResource(id = R.string.color_styles),
                            features = listOf(
                                stringResource(R.string.description_color_styles)
                            ),
                            icon = Icons.Rounded.Palette,
                            onClick = { navController.navigate(NavRoutes.ColorStyles.route) }
                        ),
                        SettingSection(
                            title = stringResource(id = R.string.Behavior),
                            features = listOf(
                                stringResource(id = R.string.description_markdown)
                            ),
                            icon = Icons.Rounded.TextFields,
                            onClick = { navController.navigate(NavRoutes.Markdown.route) }
                        ),
                        SettingSection(
                            title = stringResource(id = R.string.language),
                            features = listOf(
                                stringResource(R.string.description_language)
                            ),
                            icon = Icons.Rounded.Language,
                            onClick = { navController.navigate(NavRoutes.Language.route) }
                        )
                    )
                )
            }
            item {
                SectionBlock(
                    listOf(
                        SettingSection(
                            title = stringResource(id = R.string.backup),
                            features = listOf(
                                stringResource(R.string.description_cloud)
                            ),
                            icon = Icons.Rounded.Cloud,
                            onClick = { navController.navigate(NavRoutes.Cloud.route) }
                        ),
                        SettingSection(
                            title = stringResource(id = R.string.privacy),
                            features = listOf(
                                stringResource(id = R.string.screen_protection)
                            ),
                            icon = ImageVector.vectorResource(id = R.drawable.incognito_fill),
                            onClick = { navController.navigate(NavRoutes.Privacy.route) }
                        ),
                        SettingSection(
                            title = stringResource(id = R.string.tools),
                            features = listOf(
                                stringResource(R.string.description_tools)
                            ),
                            icon = Icons.Rounded.Work,
                            onClick = { navController.navigate(NavRoutes.Tools.route) }
                        )
                    )
                )
            }
            item {
                SectionBlock(
                    listOf(
                        SettingSection(
                            title = stringResource(id = R.string.about),
                            features = listOf(
                                stringResource(R.string.description_about)
                            ),
                            icon = Icons.Rounded.Info,
                            onClick = { navController.navigate(NavRoutes.About.route) }
                        )
                    )
                )
            }
        }
    }
}
