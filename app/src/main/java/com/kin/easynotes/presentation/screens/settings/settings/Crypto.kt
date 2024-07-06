package com.kin.easynotes.presentation.screens.settings.settings

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CurrencyBitcoin
import androidx.compose.material.icons.rounded.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.kin.easynotes.R
import com.kin.easynotes.core.constant.SupportConst
import com.kin.easynotes.presentation.navigation.NavRoutes
import com.kin.easynotes.presentation.screens.settings.SettingsScaffold
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel
import com.kin.easynotes.presentation.screens.settings.widgets.ActionType
import com.kin.easynotes.presentation.screens.settings.widgets.SettingsBox

@Composable
fun SupportScreen(navController: NavController, settingsViewModel: SettingsViewModel) {
    SettingsScaffold(
        settingsViewModel = settingsViewModel,
        title = stringResource(id = R.string.cryptocurrency),
        onBackNavClicked = { navController.navigateUp() }
    ) {
        LazyColumn {
            item {
                SettingsBox(
                    title = "Ethereum",
                    actionType = ActionType.CLIPBOARD,
                    radius = shapeManager(isFirst = true, radius = settingsViewModel.settings.value.cornerRadius),
                    clipboardText = SupportConst.ETHERIUM_ADDRESS
                )
            }
            item {
                SettingsBox(
                    title = "BNB SMART CHAIN",
                    actionType = ActionType.CLIPBOARD,
                    radius = shapeManager(radius = settingsViewModel.settings.value.cornerRadius),
                    clipboardText = SupportConst.BNB_SMART_ADDRESS
                )
            }
            item {
                SettingsBox(
                    title = "Tron",
                    actionType = ActionType.CLIPBOARD,
                    radius = shapeManager(radius = settingsViewModel.settings.value.cornerRadius),
                    clipboardText = SupportConst.TRON_ADDRESS
                )
            }
            item {
                SettingsBox(
                    title = "Polygon",
                    actionType = ActionType.CLIPBOARD,
                    radius = shapeManager( radius = settingsViewModel.settings.value.cornerRadius),
                    clipboardText = SupportConst.POLYGON_ADDRESS
                )
            }
            item {
                SettingsBox(
                    title = "Avalanche",
                    actionType = ActionType.CLIPBOARD,
                    radius = shapeManager(isLast = true, radius = settingsViewModel.settings.value.cornerRadius),
                    clipboardText = SupportConst.AVALANCHE_ADDRESS
                )
            }
        }
    }

}