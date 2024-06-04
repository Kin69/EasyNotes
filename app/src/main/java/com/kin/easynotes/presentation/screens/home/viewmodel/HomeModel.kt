package com.kin.easynotes.presentation.screens.home.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kin.easynotes.Notes
import com.kin.easynotes.domain.usecase.NoteUseCase

open class HomeViewModel() : ViewModel() {
    private val noteRepository = Notes.dataModule.noteRepository
    val noteUseCase = NoteUseCase(noteRepository, viewModelScope)

    var selectedNotes = mutableStateListOf<Int>()

    private var _isDeleteMode = mutableStateOf(false)
    val isDeleteMode: State<Boolean> = _isDeleteMode

    private var _isSelectingMode = mutableStateOf(false)
    val isSelectingMode: State<Boolean> = _isSelectingMode

    fun toggleIsSelectingMode(enabled: Boolean) {
        _isSelectingMode.value = enabled
    }

    fun toggleIsDeleteMode(enabled: Boolean) {
        _isDeleteMode.value = enabled
    }

    fun toggleNoteSelection(id: Int) {
        if (selectedNotes.contains(id)) {
            selectedNotes.remove(id)
            if (selectedNotes.isEmpty()) {
                toggleIsDeleteMode(false)
                toggleIsSelectingMode(false)
            }
        } else {
            selectedNotes.add(id)
        }
    }
}
