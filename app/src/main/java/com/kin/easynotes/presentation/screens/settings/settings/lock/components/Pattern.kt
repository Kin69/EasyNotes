package com.kin.easynotes.presentation.screens.settings.settings.lock.components

import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import com.kin.easynotes.presentation.screens.settings.settings.lock.viewModel.LockScreenViewModel
import kotlin.math.sqrt
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kin.easynotes.R
import com.kin.easynotes.presentation.navigation.NavRoutes
import com.kin.easynotes.presentation.popUpToTop
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PatternLock(
    settingsViewModel: SettingsViewModel,
    lockScreenViewModel: LockScreenViewModel = viewModel(),
    navController: NavController
) {
    val context = LocalContext.current

    BackHandler {
        if (settingsViewModel.settings.value.pattern != null) {
            (context as? ComponentActivity)?.finish()
        } else {
            navController.navigateUp()
        }
    }

    val rowCount = 3
    val columnCount = 3

    val dotColor = MaterialTheme.colorScheme.primary
    val pathColor = MaterialTheme.colorScheme.primary

    Column(
        modifier = Modifier
            .padding(vertical = 64.dp)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val text = when {
            settingsViewModel.settings.value.pattern == null -> stringResource(id = R.string.setup_pattern)
            settingsViewModel.settings.value.pattern != null -> stringResource(id = R.string.lock_pattern)
            else -> "Unknown action"
        }

        TitleText(text = text)

        Canvas(
            modifier = Modifier
                .width(300.dp)
                .height(300.dp)
                .background(MaterialTheme.colorScheme.background)
                .onSizeChanged { size ->
                    lockScreenViewModel.canvasSize = Size(size.width.toFloat(), size.height.toFloat())
                }
                .pointerInteropFilter {
                    when (it.action) {
                        MotionEvent.ACTION_DOWN -> {
                            lockScreenViewModel.canvasSize
                                .takeIf { it != Size.Zero }
                                ?.let { size ->
                                    val cell = getNearestCell(Offset(it.x, it.y), size.width, size.height)
                                    if (cell != null) {
                                        lockScreenViewModel.clearPattern()
                                        lockScreenViewModel.selectedCellsIndexList.add(cell.index)
                                        lockScreenViewModel.selectedCellCenterList.add(cell.center)
                                        lockScreenViewModel.lastCellCenter = cell.center
                                        lockScreenViewModel.path = Path().apply { moveTo(cell.center.x, cell.center.y) }
                                    }
                                }
                        }

                        MotionEvent.ACTION_MOVE -> {
                            val touchOffset = Offset(it.x, it.y)
                            lockScreenViewModel.currentTouchOffset = touchOffset

                            lockScreenViewModel.canvasSize
                                .takeIf { it != Size.Zero }
                                ?.let { size ->
                                    val cell = getNearestCell(touchOffset, size.width, size.height)

                                    if (cell != null && cell.index !in lockScreenViewModel.selectedCellsIndexList) {
                                        lockScreenViewModel.selectedCellsIndexList.add(cell.index)
                                        lockScreenViewModel.selectedCellCenterList.add(cell.center)

                                        lockScreenViewModel.updatePath(cell.center)
                                    } else if (cell == null) {
                                        // Prevent invalid lines from being drawn by skipping path updates
                                        lockScreenViewModel.currentTouchOffset = null
                                    }
                                }
                        }


                        MotionEvent.ACTION_UP -> {
                            if (settingsViewModel.settings.value.pattern == null) {
                                settingsViewModel.update(
                                    settingsViewModel.settings.value.copy(
                                        passcode = null,
                                        pattern = lockScreenViewModel.selectedCellsIndexList.joinToString(""),
                                        fingerprint = false
                                    )
                                )
                                settingsViewModel.updateDefaultRoute(NavRoutes.LockScreen.createRoute(null),)
                            } else {
                                if (settingsViewModel.settings.value.pattern == lockScreenViewModel.selectedCellsIndexList.joinToString("")) {
                                    navController.navigate(NavRoutes.Home.route) { popUpToTop(navController) }
                                }
                            }
                            lockScreenViewModel.clearPattern()
                        }
                    }
                    true
                }
        ) {
            val width = size.width
            val height = size.height

            val boxSizeInX = width / columnCount
            val boxCenterInX = boxSizeInX / 2
            val boxSizeInY = height / rowCount
            val boxCenterInY = boxSizeInY / 2

            for (row in 0 until rowCount) {
                for (column in 0 until columnCount) {
                    drawCircle(
                        color = dotColor,
                        radius = 25f,
                        center = Offset(
                            (boxCenterInX + boxSizeInX * column),
                            (boxCenterInY + boxSizeInY * row)
                        )
                    )
                }
            }

            drawPath(
                path = lockScreenViewModel.path,
                color = pathColor,
                style = Stroke(
                    width = 20f,
                    cap = StrokeCap.Round
                )
            )

            lockScreenViewModel.currentTouchOffset?.let { offset ->
                lockScreenViewModel.lastCellCenter?.let { lastCenter ->
                    val connectingPath = Path().apply {
                        moveTo(lastCenter.x, lastCenter.y)
                        lineTo(offset.x, offset.y)
                    }
                    drawPath(
                        path = connectingPath,
                        color = pathColor,
                        style = Stroke(
                            width = 20f,
                            cap = StrokeCap.Round
                        )
                    )
                }
            }
        }
    }
}


data class CellModel(
    val index: Int,
    val center: Offset
)

private fun getNearestCell(offset: Offset, width: Float, height: Float): CellModel? {
    val rowCount = 3
    val columnCount = 3

    val boxSizeInX = width / columnCount
    val boxCenterInX = boxSizeInX / 2
    val boxSizeInY = height / rowCount
    val boxCenterInY = boxSizeInY / 2

    val circleRadius = width / 8

    for (row in 0 until rowCount) {
        for (column in 0 until columnCount) {
            val cellCenter = Offset(
                (boxCenterInX + boxSizeInX * column),
                (boxCenterInY + boxSizeInY * row)
            )
            val distanceFromCenter = sqrt(
                (offset.x - cellCenter.x) * (offset.x - cellCenter.x) +
                        (offset.y - cellCenter.y) * (offset.y - cellCenter.y)
            )

            if (distanceFromCenter < circleRadius) {
                val index = column + 1 + row * columnCount
                return CellModel(index, cellCenter)
            }
        }
    }
    return null
}