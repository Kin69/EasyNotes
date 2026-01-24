package com.kin.easynotes.presentation.screens.settings

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kin.easynotes.R
import com.kin.easynotes.data.billing.BillingManager
import com.kin.easynotes.data.billing.PurchaseState
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportContent(
    navController: NavController,
    settingsViewModel: SettingsViewModel,
    onExit: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity

    val billingManager = remember { BillingManager(context) }
    val purchaseState by billingManager.purchaseState.collectAsState()

    val thankYouMessage = stringResource(R.string.support_thank_you)
    val cancelledMessage = stringResource(R.string.support_purchase_cancelled)
    val errorMessage = stringResource(R.string.support_purchase_error)

    LaunchedEffect(Unit) {
        activity?.let { billingManager.launchPurchaseFlow(it) }
    }

    LaunchedEffect(purchaseState) {
        when (purchaseState) {
            is PurchaseState.Purchased -> {
                Toast.makeText(context, thankYouMessage, Toast.LENGTH_LONG).show()
                billingManager.resetState()
                onExit()
            }
            is PurchaseState.Cancelled -> {
                Toast.makeText(context, cancelledMessage, Toast.LENGTH_SHORT).show()
                billingManager.resetState()
                onExit()
            }
            is PurchaseState.Error -> {
                val error = purchaseState as PurchaseState.Error
                Toast.makeText(context, "$errorMessage: ${error.message}", Toast.LENGTH_LONG).show()
                billingManager.resetState()
                onExit()
            }
            else -> {}
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            billingManager.endConnection()
        }
    }

    // Show loading indicator while connecting
    if (purchaseState is PurchaseState.Idle || purchaseState is PurchaseState.Connecting) {
        ModalBottomSheet(
            containerColor = MaterialTheme.colorScheme.background,
            onDismissRequest = { onExit() }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
