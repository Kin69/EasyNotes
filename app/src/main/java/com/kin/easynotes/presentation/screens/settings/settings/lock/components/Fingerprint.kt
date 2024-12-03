package com.kin.easynotes.presentation.screens.settings.settings.lock.components

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.kin.easynotes.presentation.screens.settings.settings.lock.viewModel.LockScreenViewModel
import com.kin.easynotes.R
import com.kin.easynotes.presentation.navigation.NavRoutes
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel
import java.util.concurrent.Executor


@Composable
fun FingerprintLock(
    settingsViewModel: SettingsViewModel,
    navController: NavController
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.1f)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.fingerprint_name),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        LaunchedEffect(Unit) {
            customizedPrompt(context, settingsViewModel, navController)
        }
    }
}

fun customizedPrompt(
    context: Context,
    settingsViewModel: SettingsViewModel,
    navController: NavController
) {
    showBiometricPrompt(
        context,
        context as AppCompatActivity,
        onAuthError = {
            customizedPrompt(context, settingsViewModel, navController)
        },
        onAuthSuccess = {
            settingsViewModel.update(
                settingsViewModel.settings.value.copy(
                    passcode = null,
                    fingerprint = true,
                    pattern = null
                )
            )
            navController.navigate(NavRoutes.Home.route)
        }
    )
}

fun showBiometricPrompt(
    context: Context,
    activity: AppCompatActivity,
    onAuthSuccess: () -> Unit,
    onAuthError: (String) -> Unit
) {
    val executor: Executor = ContextCompat.getMainExecutor(activity)

    val biometricPrompt = BiometricPrompt(
        activity,
        executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onAuthSuccess()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                onAuthError(errString.toString())
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                onAuthError("Authentication failed. Please try again.")
            }
        })

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle(context.getString(R.string.fingerprint_name))
        .setSubtitle(context.getString(R.string.app_name))
        .setNegativeButtonText(context.getString(R.string.cancel))
        .setConfirmationRequired(true)
        .build()

    biometricPrompt.authenticate(promptInfo)
}
