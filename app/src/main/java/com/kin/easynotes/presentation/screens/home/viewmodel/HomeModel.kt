package com.kin.easynotes.presentation.screens.home.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.kin.easynotes.domain.usecase.NoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val noteUseCase: NoteUseCase
) : ViewModel() {
    var selectedNotes = mutableStateListOf<Int>()

    private var _isSelectMenuOpened = mutableStateOf(false)
    val isSelectMenuOpened: State<Boolean> = _isSelectMenuOpened

    private var _isDeleteMode = mutableStateOf(false)
    val isDeleteMode: State<Boolean> = _isDeleteMode

    private var _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    fun toggleIsDeleteMode(enabled: Boolean) {
        _isDeleteMode.value = enabled
    }

    fun toggleSelectMenu(enabled: Boolean) {
        _isSelectMenuOpened.value = enabled
    }

    fun changeSearchQuery(newValue: String) {
        _searchQuery.value = newValue
    }
}
