package com.kin.easynotes.presentation.screens.settings.settings

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.rounded.DoDisturbAlt
import androidx.compose.material.icons.rounded.LockReset
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kin.easynotes.R
import com.kin.easynotes.presentation.navigation.NavRoutes
import com.kin.easynotes.presentation.screens.settings.SettingsScaffold
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel
import com.kin.easynotes.presentation.screens.settings.widgets.ActionType
import com.kin.easynotes.presentation.screens.settings.widgets.CustomListDialog
import com.kin.easynotes.presentation.screens.settings.widgets.SettingsBox

@Composable
fun PrivacyScreen(navController: NavController, settingsViewModel: SettingsViewModel) {
    SettingsScaffold(
        settingsViewModel = settingsViewModel,
        title = stringResource(id = R.string.privacy),
        onBackNavClicked = { navController.navigateUp() }
    ) {
        LazyColumn {
            item {
                SettingsBox(
                    title = stringResource(id = R.string.screen_protection),
                    description = stringResource(id = R.string.screen_protection_description),
                    icon = Icons.Filled.RemoveRedEye,
                    radius = shapeManager(radius = settingsViewModel.settings.value.cornerRadius, isBoth = true),
                    actionType = ActionType.SWITCH,
                    variable = settingsViewModel.settings.value.screenProtection,
                    switchEnabled = { settingsViewModel.update(settingsViewModel.settings.value.copy(screenProtection = it)) },
                )
                Spacer(modifier = Modifier.height(18.dp))
            }
            item {
                SettingsBox(
                    title = stringResource(id = R.string.vault),
                    description = stringResource(id = R.string.vault_description),
                    icon = Icons.Rounded.Security,
                    radius = shapeManager(radius = settingsViewModel.settings.value.cornerRadius, isFirst = true),
                    actionType = ActionType.SWITCH,
                    variable = settingsViewModel.settings.value.vaultSettingEnabled,

                    switchEnabled = { settingsViewModel.update(settingsViewModel.settings.value.copy(vaultSettingEnabled = it)) },
                )
            }
            item {
                SettingsBox(
                    radius = shapeManager(radius = settingsViewModel.settings.value.cornerRadius, isLast = true),
                    title = stringResource(id = R.string.app_lock),
                    description = stringResource(id = R.string.app_lock_description),
                    icon = Icons.Filled.Lock,
                    actionType = ActionType.CUSTOM,
                    customAction = { onExit ->
                        OnLockClicked(onExit = { onExit() }, navController = navController, settings = settingsViewModel)
                    },
                )
                Spacer(modifier = Modifier.height(18.dp))
            }
            item {
                SettingsBox(
                    isEnabled = settingsViewModel.settings.value.passcode != null || settingsViewModel.settings.value.pattern != null || settingsViewModel.settings.value.fingerprint,
                    title = stringResource(id = R.string.lock_on_resume),
                    description = stringResource(id = R.string.lock_on_resume_description),
                    icon = Icons.Rounded.LockReset,
                    radius = shapeManager(radius = settingsViewModel.settings.value.cornerRadius, isBoth = true),
                    actionType = ActionType.SWITCH,
                    variable = settingsViewModel.settings.value.lockImmediately,

                    switchEnabled = { settingsViewModel.update(settingsViewModel.settings.value.copy(lockImmediately = it)) },
                )
            }
        }
    }
}


@Composable
private fun OnLockClicked(
    settings: SettingsViewModel,
    onExit: () -> Unit,
    navController: NavController
) {
    CustomListDialog(
        text = stringResource(id = R.string.app_lock),
        onExit = onExit
    ) {
        item {
            createSettingBox(
                title = stringResource(id = R.string.passcode),
                description = stringResource(id = R.string.authorize_passcode),
                isEnabled = settings.settings.value.passcode.isNullOrBlank(),
                onAction = {
                    onExit()
                    if (settings.settings.value.passcode.isNullOrBlank()) {
                        navController.navigate(NavRoutes.LockScreen.createRoute(com.kin.easynotes.presentation.navigation.ActionType.PASSCODE))
                    } else {
                        settings.update(settings.settings.value.copy(passcode = null, defaultRouteType = NavRoutes.Home.route))
                    }
                }
            )
            createSettingBox(
                title = stringResource(id = R.string.fingerprint),
                description = stringResource(id = R.string.authorize_fingerprint),
                isEnabled = !settings.settings.value.fingerprint,
                onAction = {
                    onExit()
                    if (!settings.settings.value.fingerprint) {
                        navController.navigate(NavRoutes.LockScreen.createRoute(com.kin.easynotes.presentation.navigation.ActionType.FINGERPRINT))
                    } else {
                        settings.update(settings.settings.value.copy(fingerprint = false, defaultRouteType = NavRoutes.Home.route))
                    }
                }
            )
            createSettingBox(
                title = stringResource(id = R.string.pattern),
                description = stringResource(id = R.string.authorize_pattern),
                isEnabled = settings.settings.value.pattern.isNullOrBlank(),
                onAction = {
                    onExit()
                    if (settings.settings.value.pattern.isNullOrBlank()) {
                        navController.navigate(NavRoutes.LockScreen.createRoute(com.kin.easynotes.presentation.navigation.ActionType.PATTERN))
                    } else {
                        settings.update(settings.settings.value.copy(pattern = null, defaultRouteType = NavRoutes.Home.route))
                        settings.updateDefaultRoute(NavRoutes.Home.route)
                    }
                }
            )
        }
    }
}

@Composable
private fun createSettingBox(
    title: String,
    description: String,
    isEnabled: Boolean,
    onAction: () -> Unit
) {
    SettingsBox(
        title = title,
        description = description,
        actionType = ActionType.CUSTOM,
        customAction = {
            LaunchedEffect(Unit) {
                onAction()
            }
        },
        customButton = {
            Icon(
                imageVector = if (isEnabled) {
                    Icons.AutoMirrored.Rounded.ArrowForwardIos
                } else {
                    Icons.Rounded.DoDisturbAlt
                },
                contentDescription = "",
                modifier = Modifier.scale(0.75f)
            )
        }
    )
}
