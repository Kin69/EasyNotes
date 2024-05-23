package com.kin.easynotes.presentation.screens.edit.model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.kin.easynotes.domain.usecase.NoteViewModel

class EditViewModel : NoteViewModel() {
    private val _noteNameState = mutableStateOf("")
    val noteNameState: State<String> = _noteNameState

    private val _noteDescriptionState =  mutableStateOf("")
    val noteDescriptionState: State<String> = _noteDescriptionState

    var noteDeleteState: Boolean = false
    fun updateNoteNameState(newName: String) {
        _noteNameState.value = newName
    }

    fun updateNoteNameDescription(newDescription: String) {
        _noteDescriptionState.value = newDescription
    }

}