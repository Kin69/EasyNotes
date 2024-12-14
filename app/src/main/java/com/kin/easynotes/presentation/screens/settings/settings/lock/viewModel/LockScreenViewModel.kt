package com.kin.easynotes.presentation.screens.settings.settings.lock.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kin.easynotes.presentation.navigation.NavRoutes
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LockScreenViewModel : ViewModel() {
    val selectedCellsIndexList = mutableStateListOf<Int?>()
    val selectedCellCenterList = mutableStateListOf<Offset>()
    var canvasSize by mutableStateOf(Size.Zero)
    var currentTouchOffset by mutableStateOf<Offset?>(null)
    var lastCellCenter by mutableStateOf<Offset?>(null)
    var path by mutableStateOf(Path())

    private val _pinCode : MutableState<List<Int>> = mutableStateOf(emptyList())
    val pinCode: State<List<Int>> = _pinCode

    private val _isPinIncorrect = mutableStateOf(false)
    val isPinIncorrect: State<Boolean> = _isPinIncorrect

    private val _animateError = mutableStateOf(false)
    val animateError: State<Boolean> = _animateError

    fun updatePath(cellCenter: Offset) {
        lastCellCenter?.let {
            path.lineTo(it.x, it.y)
        }
        lastCellCenter = cellCenter
        path.lineTo(cellCenter.x, cellCenter.y)
    }


    fun clearPattern() {
        selectedCellsIndexList.clear()
        selectedCellCenterList.clear()
        path = Path()
        currentTouchOffset = null
        lastCellCenter = null
    }

    fun addNumber(settingsViewModel: SettingsViewModel, number: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            if (_pinCode.value.size < 6) {
                _pinCode.value += number
                if (pinCode.value.size == 6) {
                    onResult(checkPinCode(settingsViewModel))
                }
            }
        }
    }

    fun removeNumber() {
        viewModelScope.launch {
            if (_pinCode.value.isNotEmpty()) {
                _pinCode.value = _pinCode.value.dropLast(1)
            }
        }
    }

    private fun checkPinCode(settingsViewModel: SettingsViewModel): Boolean {
        if (settingsViewModel.settings.value.passcode == null) {
            settingsViewModel.update(
                settingsViewModel.settings.value.copy(
                    passcode = _pinCode.value.joinToString(""),
                    fingerprint = false,
                    pattern = null
                )
            )
            settingsViewModel.updateDefaultRoute(NavRoutes.LockScreen.createRoute(null),)

            return true
        } else {
            if (_pinCode.value.joinToString("") == settingsViewModel.settings.value.passcode) {
                return true
            } else {
                _isPinIncorrect.value = true
                _animateError.value = true
                viewModelScope.launch {
                    delay(500)
                    _animateError.value = false
                    onReset()
                }
                return false
            }
        }
    }

    fun onReset() {
        _pinCode.value = emptyList()
        _isPinIncorrect.value = false
        _animateError.value = false
    }
}