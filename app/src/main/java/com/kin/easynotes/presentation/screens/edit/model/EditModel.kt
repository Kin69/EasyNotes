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

    fun toggleBold() {
        _noteDescriptionState.value = toggleMarkdownFormat(_noteDescriptionState.value, "<strong>")
    }

    fun toggleItalic() {
        _noteDescriptionState.value = toggleMarkdownFormat(_noteDescriptionState.value, "_")
    }

    private fun toggleMarkdownFormat(text: String, format: String): String {
        return if (text.startsWith(format) && text.endsWith(format)) {
            text.substring(format.length, text.length - format.length)
        } else {
            "$format$text$format"
        }
    }
}