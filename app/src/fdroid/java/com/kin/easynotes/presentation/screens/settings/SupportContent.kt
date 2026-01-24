package com.kin.easynotes.presentation.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Coffee
import androidx.compose.material.icons.rounded.CurrencyBitcoin
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kin.easynotes.R
import com.kin.easynotes.core.constant.ConnectionConst
import com.kin.easynotes.presentation.navigation.NavRoutes
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel
import com.kin.easynotes.presentation.screens.settings.settings.shapeManager
import com.kin.easynotes.presentation.screens.settings.widgets.ActionType
import com.kin.easynotes.presentation.screens.settings.widgets.SettingsBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportContent(
    navController: NavController,
    settingsViewModel: SettingsViewModel,
    onExit: () -> Unit
) {
    val uriHandler = LocalUriHandler.current

    ModalBottomSheet(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        onDismissRequest = { onExit() }
    ) {
        Column(
            modifier = Modifier.padding(20.dp, 0.dp, 20.dp, 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.support_foss_title),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.support_foss_description),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            SettingsBox(
                size = 8.dp,
                title = "Ko-fi",
                icon = Icons.Rounded.Coffee,
                isCentered = true,
                actionType = ActionType.LINK,
                radius = shapeManager(isFirst = true, radius = settingsViewModel.settings.value.cornerRadius),
                linkClicked = { uriHandler.openUri(ConnectionConst.SUPPORT_KOFI) }
            )
            SettingsBox(
                title = "Libera Pay",
                size = 8.dp,
                isCentered = true,
                icon = Icons.Rounded.Payments,
                radius = shapeManager(radius = settingsViewModel.settings.value.cornerRadius),
                actionType = ActionType.LINK,
                linkClicked = { uriHandler.openUri(ConnectionConst.SUPPORT_LIBERAPAY) }
            )
            SettingsBox(
                title = stringResource(R.string.cryptocurrency),
                size = 8.dp,
                icon = Icons.Rounded.CurrencyBitcoin,
                isCentered = true,
                actionType = ActionType.CUSTOM,
                radius = shapeManager(radius = settingsViewModel.settings.value.cornerRadius, isLast = true),
                customAction = {
                    onExit()
                    navController.navigate(NavRoutes.Support.route)
                }
            )
        }
    }
}
