package com.kin.easynotes.presentation.screens.settings.settings.lock.components

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Backspace
import androidx.compose.material.icons.rounded.Fingerprint
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kin.easynotes.presentation.screens.settings.settings.lock.viewModel.LockScreenViewModel
import com.kin.easynotes.R
import com.kin.easynotes.presentation.navigation.NavRoutes
import com.kin.easynotes.presentation.popUpToTop
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel

@Composable
fun PasscodeLock(
    settingsViewModel: SettingsViewModel,
    navController: NavController,
    passcodeLockViewModel: LockScreenViewModel = viewModel(),
) {
    val context = LocalContext.current

    BackHandler {
        if (settingsViewModel.settings.value.passcode != null) {
            (context as? ComponentActivity)?.finish()
        } else {
            navController.navigateUp()
        }
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 32.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.2f))

            val text = if (settingsViewModel.settings.value.passcode.isNullOrBlank()) stringResource(id = R.string.setup_password) else stringResource(
                id = R.string.enter_password
            )

            TitleText(text = text)

            PinCodeDisplay(
                pinCode = passcodeLockViewModel.pinCode.value,
                isPinIncorrect = passcodeLockViewModel.isPinIncorrect.value,
                animateError = passcodeLockViewModel.animateError.value
            )
        }

        Column {
            NumberPad(
                numbers = (1..9).toList(),
                onNumberClick = { number ->
                    passcodeLockViewModel.addNumber(settingsViewModel, number) { result ->
                        if (result) {
                            navController.navigate(NavRoutes.Home.route) { popUpToTop(navController) }
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.height(24.dp))

            ActionRow(
                onFingerprintClick = {
                    passcodeLockViewModel.onReset()
                },
                onZeroClick = {
                    passcodeLockViewModel.addNumber(settingsViewModel, 0) { result ->
                        if (result) {
                            navController.navigate(NavRoutes.Home.route) { popUpToTop(navController) }
                        }
                    }
                },
                onBackspaceClick = {
                    passcodeLockViewModel.removeNumber()
                }
            )

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
fun PinCodeDisplay(pinCode: List<Int>, isPinIncorrect: Boolean, animateError: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        repeat(6) { index ->
            Box(
                modifier = Modifier
                    .size(55.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .border(
                        width = 2.dp,
                        color = getBorderColor(index < pinCode.size, isPinIncorrect, animateError),
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (index < pinCode.size) {
                    PinCodeDot()
                }
            }
        }
    }
}

@Composable
fun PinCodeDot() {
    Box(
        modifier = Modifier
            .size(15.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
    )
}

@Composable
fun getBorderColor(isFilled: Boolean, isPinIncorrect: Boolean, animateError: Boolean): Color {
    val targetColor = if (isPinIncorrect && animateError) MaterialTheme.colorScheme.error else Color.Transparent
    return if (isFilled) {
        animateColorAsState(
            targetValue = targetColor,
            animationSpec = tween(durationMillis = 600), label = ""
        ).value
    } else {
        Color.Transparent
    }
}

@Composable
fun ActionRow(
    onFingerprintClick: () -> Unit,
    onZeroClick: () -> Unit,
    onBackspaceClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 48.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = Icons.Rounded.Fingerprint,
            contentDescription = "Fingerprint",
            modifier = Modifier
                .scale(2f)
                .clickable(onClick = onFingerprintClick),
            tint = MaterialTheme.colorScheme.primary
        )
        NumberButton(
            number = 0,
        ) {
            onZeroClick()
        }
        Icon(
            imageVector = Icons.Rounded.Backspace,
            contentDescription = "Backspace",
            modifier = Modifier
                .scale(1.5f)
                .clickable(onClick = onBackspaceClick),
            tint = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
fun NumberPad(
    numbers: List<Int>,
    onNumberClick: (Int) -> Unit
) {
    Column {
        numbers.chunked(3).forEach { rowNumbers ->
            NumberRow(
                rowNumbers = rowNumbers,
                onNumberClick = onNumberClick
            )
        }
    }
}

@Composable
fun NumberRow(
    rowNumbers: List<Int>,
    onNumberClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        rowNumbers.forEach { number ->
            NumberButton(
                modifier = Modifier
                    .height(84.dp)
                    .weight(1f),
                number = number,
                onClick = onNumberClick
            )
        }
        if (rowNumbers.size < 3) {
            repeat(3 - rowNumbers.size) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun NumberButton(
    modifier: Modifier = Modifier,
    number: Int,
    onClick: (Int) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick(number) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = number.toString(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp
        )
    }
}
