package com.kin.easynotes.presentation.screens.settings.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Colorize
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.FlashOn
import androidx.compose.material.icons.rounded.GridOff
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.HdrAuto
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.RoundedCorner
import androidx.compose.material.icons.rounded.ViewAgenda
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.kin.easynotes.R
import com.kin.easynotes.presentation.screens.settings.SettingsScaffold
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel
import com.kin.easynotes.presentation.screens.settings.widgets.ActionType
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
        onBackNavClicked = { navController.popBackStack() }
    ) {
        LazyColumn {
            item {
                SettingsBox(
                    title = stringResource(id = R.string.system_theme),
                    icon = Icons.Rounded.HdrAuto,
                    actionType = ActionType.SWITCH,
                    radius = shapeManager(isFirst = true, radius = settingsViewModel.settings.value.cornerRadius),
                    variable = settingsViewModel.settings.value.automaticTheme,
                    switchEnabled = { settingsViewModel.update(settingsViewModel.settings.value.copy(automaticTheme = it))}
                )
            }
            item {
                SettingsBox(
                    title = stringResource(id = R.string.dark_theme),
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
                    icon = Icons.Rounded.Colorize,
                    radius = shapeManager(radius = settingsViewModel.settings.value.cornerRadius),
                    isEnabled = !settingsViewModel.settings.value.automaticTheme,
                    actionType = ActionType.SWITCH,
                    variable = settingsViewModel.settings.value.dynamicTheme,
                    switchEnabled = { settingsViewModel.update(settingsViewModel.settings.value.copy(automaticTheme = false, dynamicTheme = it))}
                )
            }
            item {
                SettingsBox(
                    title = stringResource(id = R.string.amoled_colors),
                    icon = Icons.Rounded.DarkMode,
                    radius = shapeManager(radius = settingsViewModel.settings.value.cornerRadius, isLast = true),
                    actionType = ActionType.SWITCH,
                    isEnabled = settingsViewModel.settings.value.darkTheme,
                    variable = settingsViewModel.settings.value.amoledTheme,
                    switchEnabled = { settingsViewModel.update(settingsViewModel.settings.value.copy(amoledTheme = it))}
                )
                Spacer(modifier = Modifier.height(18.dp))
            }
            item {
                SettingsBox(
                    title = stringResource(id = R.string.radius),
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
                    title = when(settingsViewModel.settings.value.viewMode) {
                        true -> stringResource(id = R.string.grid_view)
                        false -> stringResource(id = R.string.column_view)
                    },
                    icon = when(settingsViewModel.settings.value.viewMode) {
                        true -> Icons.Rounded.GridView
                        false -> Icons.Rounded.ViewAgenda
                    },
                    radius = shapeManager(radius = settingsViewModel.settings.value.cornerRadius),
                    actionType = ActionType.SWITCH,
                    variable = settingsViewModel.settings.value.viewMode,
                    switchEnabled = { settingsViewModel.update(settingsViewModel.settings.value.copy(viewMode = it))}
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
                    color = MaterialTheme.colorScheme.surfaceContainerLow
                )
                .height(62.dp),
        )
    }

    Dialog(onDismissRequest = { onExit(realRadius) }) {
        Card(shape = RoundedCornerShape(realRadius/3),) {
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

