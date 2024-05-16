package com.kin.easynotes.presentation.screens.edit.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kin.easynotes.domain.usecase.NoteViewModel

class EditViewModel : NoteViewModel() {
    var noteNameState by mutableStateOf("")
    var noteDescriptionState by mutableStateOf("")
    var noteDeleteState by mutableStateOf(false)
    fun updateNoteNameState(string: String) {
        noteNameState = string
    }

    fun updateNoteDeleteState(boolean: Boolean) {
        noteDeleteState = boolean
    }
    fun updateNoteNameDescription(string: String) {
        noteDescriptionState = string
    }
}