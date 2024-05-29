package com.kin.easynotes.presentation.screens.edit.model


import androidx.compose.runtime.MutableState
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
    var noteInfoState: MutableState<Boolean> = mutableStateOf(false)


    fun updateNoteNameState(newName: TextFieldValue) {
        _noteNameState.value = newName
    }


    fun updateNoteDescriptionState(newDescription: TextFieldValue) {
        _noteDescriptionState.value = newDescription
    }

    fun insertText(insertText: String, offset : Int = 1) {
        val text = _noteDescriptionState.value.text.let {
            if (it.isNotEmpty()) "$it\n" else it
        }

        _noteDescriptionState.value = TextFieldValue(text = "$text$insertText",selection = TextRange(text.length + insertText.length + offset))
    }

}
