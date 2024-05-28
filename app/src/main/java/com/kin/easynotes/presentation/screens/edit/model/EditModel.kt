package com.kin.easynotes.presentation.screens.edit.model


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.kin.easynotes.domain.usecase.NoteViewModel


class EditViewModel : NoteViewModel() {
    private val _noteNameState = mutableStateOf(TextFieldValue(""))
    val noteNameState: State<TextFieldValue> get() = _noteNameState

    private val _noteDescriptionState = mutableStateOf(TextFieldValue(""))
    val noteDescriptionState: State<TextFieldValue> get() = _noteDescriptionState

    var noteDeleteState: Boolean = false

    fun updateNoteNameState(newName: TextFieldValue) {
        _noteNameState.value = newName
    }


    fun updateNoteDescriptionState(newDescription: TextFieldValue) {
        _noteDescriptionState.value = newDescription
    }

    fun insertText(insertText: String, offset: Int = 1) {
        val textFieldValue = _noteDescriptionState.value
        val text = textFieldValue.text
        val selection = textFieldValue.selection
        val cursorPosition = selection.start
        val lastNewLineIndex = text.lastIndexOf('\n', cursorPosition - 1)
        val isCurrentLineEmpty = if (lastNewLineIndex == -1) {
            text.substring(0, cursorPosition).isEmpty()
        } else {
            text.substring(lastNewLineIndex + 1, cursorPosition).isEmpty()
        }
        val newText = if (!isCurrentLineEmpty) {
            "$text\n$insertText"
        } else {
            "$text$insertText"
        }
        val newCursorPosition = cursorPosition + insertText.length + if (isCurrentLineEmpty) offset else offset + 1
        _noteDescriptionState.value = TextFieldValue(
            text = newText,
            selection = TextRange(newCursorPosition)
        )
    }

}
