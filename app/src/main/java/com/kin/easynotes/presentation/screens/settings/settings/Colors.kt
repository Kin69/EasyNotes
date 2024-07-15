package com.kin.easynotes.presentation.screens.settings.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Sort
import androidx.compose.material.icons.rounded.Battery1Bar
import androidx.compose.material.icons.rounded.Colorize
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.DynamicFeed
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.HdrAuto
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.RoundedCorner
import androidx.compose.material.icons.rounded.Translate
import androidx.compose.material.icons.rounded.ViewAgenda
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.os.LocaleListCompat
import androidx.navigation.NavController
import com.kin.easynotes.R
import com.kin.easynotes.presentation.screens.settings.SettingsScaffold
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel
import com.kin.easynotes.presentation.screens.settings.widgets.ActionType
import com.kin.easynotes.presentation.screens.settings.widgets.ListDialog
import com.kin.easynotes.presentation.screens.settings.widgets.SettingsBox

fun shapeManager(isBoth: Boolean = false,isLast: Boolean = false,isFirst: Boolean = false,radius: Int): RoundedCornerShape {
    val smallerRadius: Dp = (radius/5).dp
    val defaultRadius: Dp = radius.dp

    return when {
        isBoth -> RoundedCornerShape(defaultRadius)
        isLast -> RoundedCornerShape(smallerRadius,smallerRadius,defaultRadius,defaultRadius)
        isFirst -> RoundedCornerShape(defaultRadius,defaultRadius,smallerRadius,smallerRadius)
        else -> RoundedCornerShape(smallerRadius)
    }
}

@Composable
fun ColorStylesScreen(navController: NavController, settingsViewModel: SettingsViewModel) {
    SettingsScaffold(
        settingsViewModel = settingsViewModel,
        title = stringResource(id = R.string.color_styles),
        onBackNavClicked = { navController.navigateUp() }
    ) {
        LazyColumn {
            item {
                SettingsBox(
                    title = stringResource(id = R.string.minimalistic_mode),
                    description = stringResource(id = R.string.minimalistic_mode_description),
                    icon = Icons.Rounded.DynamicFeed,
                    radius = shapeManager(radius = settingsViewModel.settings.value.cornerRadius, isBoth = true),
                    actionType = ActionType.SWITCH,
                    variable = settingsViewModel.settings.value.minimalisticMode,
                    switchEnabled = { settingsViewModel.update(settingsViewModel.settings.value.copy(minimalisticMode = it))}
                )
                Spacer(modifier = Modifier.height(18.dp))
            }
            item {
                SettingsBox(
                    title = stringResource(id = R.string.system_theme),
                    description = stringResource(id = R.string.system_theme_description),
                    icon = Icons.Rounded.HdrAuto,
                    actionType = ActionType.SWITCH,
                    radius = shapeManager(isFirst = true ,isBoth = (!isSystemInDarkTheme() && settingsViewModel.settings.value.automaticTheme), radius = settingsViewModel.settings.value.cornerRadius),
                    variable = settingsViewModel.settings.value.automaticTheme,
                    switchEnabled = { settingsViewModel.update(settingsViewModel.settings.value.copy(automaticTheme = it))}
                )
            }
            item {
                SettingsBox(
                    title = stringResource(id = R.string.dark_theme),
                    description = stringResource(id = R.string.dark_theme_description),
                    isEnabled = !settingsViewModel.settings.value.automaticTheme,
                    icon = Icons.Rounded.Palette,
                    radius = shapeManager(radius = settingsViewModel.settings.value.cornerRadius),
                    actionType = ActionType.SWITCH,
                    variable = settingsViewModel.settings.value.darkTheme,
                    switchEnabled = { settingsViewModel.update(settingsViewModel.settings.value.copy(automaticTheme = false, darkTheme = it))}
                )
            }
            item {
                SettingsBox(
                    title = stringResource(id = R.string.dynamic_colors),
                    description = stringResource(id = R.string.dynamic_colors_description),
                    icon = Icons.Rounded.Colorize,
                    radius = shapeManager(radius = settingsViewModel.settings.value.cornerRadius, isLast = !(settingsViewModel.settings.value.darkTheme)),
                    isEnabled = !settingsViewModel.settings.value.automaticTheme,
                    actionType = ActionType.SWITCH,
                    variable = settingsViewModel.settings.value.dynamicTheme,
                    switchEnabled = { settingsViewModel.update(settingsViewModel.settings.value.copy(automaticTheme = false, dynamicTheme = it))}
                )
            }
            item {
                val value = settingsViewModel.settings.value.amoledTheme
                SettingsBox(
                    title = stringResource(id = R.string.amoled_colors),
                    description = stringResource(id = R.string.amoled_colors_description),
                    icon = Icons.Rounded.DarkMode,
                    radius = shapeManager(radius = settingsViewModel.settings.value.cornerRadius, isLast = !value),
                    actionType = ActionType.SWITCH,
                    isEnabled = settingsViewModel.settings.value.darkTheme,
                    variable = value,
                    switchEnabled = { settingsViewModel.update(settingsViewModel.settings.value.copy(amoledTheme = it))}
                )
            }
            item {
                SettingsBox(
                    title = stringResource(id = R.string.extreme_amoled_mode),
                    icon = Icons.Rounded.Battery1Bar,
                    description = stringResource(id = R.string.extreme_amoled_mode_description),
                    radius = shapeManager(radius = settingsViewModel.settings.value.cornerRadius, isLast = true),
                    actionType = ActionType.SWITCH,
                    variable = settingsViewModel.settings.value.extremeAmoledMode,
                    isEnabled = settingsViewModel.settings.value.amoledTheme && settingsViewModel.settings.value.darkTheme,
                    switchEnabled = { settingsViewModel.update(settingsViewModel.settings.value.copy(extremeAmoledMode = it))}
                )
                Spacer(modifier = Modifier.height(18.dp))
            }
            item {
                SettingsBox(
                    title = stringResource(id = R.string.radius),
                    description = stringResource(id = R.string.radius_description),
                    icon = Icons.Rounded.RoundedCorner,
                    radius = shapeManager(radius = settingsViewModel.settings.value.cornerRadius, isFirst = true),
                    actionType = ActionType.CUSTOM,
                    customAction = {
                        onExit -> OnRadiusClicked(settingsViewModel) {
                            settingsViewModel.update(settingsViewModel.settings.value.copy(cornerRadius = it))
                            onExit()
                        }
                    }
                )
            }
            item {
                SettingsBox(
                    title = if (settingsViewModel.settings.value.viewMode) stringResource(id = R.string.grid_view) else stringResource(id = R.string.column_view),
                    icon = if (settingsViewModel.settings.value.viewMode) Icons.Rounded.GridView else Icons.Rounded.ViewAgenda,
                    description = stringResource(id = R.string.view_style_description),
                    radius = shapeManager(radius = settingsViewModel.settings.value.cornerRadius),
                    actionType = ActionType.SWITCH,
                    variable = settingsViewModel.settings.value.viewMode,
                    switchEnabled = { settingsViewModel.update(settingsViewModel.settings.value.copy(viewMode = it))}
                )
            }
            item {
                SettingsBox(
                    title = if (settingsViewModel.settings.value.sortDescending) stringResource(id = R.string.sort_descending) else stringResource(id = R.string.sort_ascending),
                    description = stringResource(id = R.string.sort_description),
                    icon = Icons.AutoMirrored.Rounded.Sort,
                    radius = shapeManager(radius = settingsViewModel.settings.value.cornerRadius, isLast = true),
                    actionType = ActionType.SWITCH,
                    variable = settingsViewModel.settings.value.sortDescending,
                    switchEnabled = { settingsViewModel.update(settingsViewModel.settings.value.copy(sortDescending = it)) }
                )
            }
        }
    }
}

@Composable
fun OnRadiusClicked( settingsViewModel: SettingsViewModel,onExit: (Int) -> Unit) {
    val minimalRadius = 5
    val settingsRadius = settingsViewModel.settings.value.cornerRadius
    var sliderPosition by remember { mutableFloatStateOf(((settingsRadius - minimalRadius).toFloat()/30)) }
    val realRadius : Int  = (((sliderPosition*100).toInt())/3) + minimalRadius

    @Composable
    fun example(shape: RoundedCornerShape) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp, 3.dp, 32.dp, 1.dp)
                .background(
                    shape = shape,
                    color = MaterialTheme.colorScheme.surfaceContainerHigh
                )
                .height(62.dp),
        )
    }
    Dialog(onDismissRequest = { onExit(realRadius) }) {
        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    shape = RoundedCornerShape(realRadius / 3)
                )
                .fillMaxWidth()
                .fillMaxSize(0.38f)
        ) {
            Text(
                text = stringResource(id = R.string.select_radius),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp)
            )
            example(shapeManager(radius = realRadius, isFirst = true))
            example(shapeManager(radius = realRadius))
            example(shapeManager(radius =  realRadius, isLast = true))
            Slider(
                value = sliderPosition,
                modifier = Modifier.padding(32.dp,16.dp,32.dp,16.dp),
                colors = SliderDefaults.colors(inactiveTrackColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                onValueChange = { newValue -> sliderPosition = newValue}
            )
        }
    }
}

