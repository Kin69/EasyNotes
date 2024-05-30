package com.kin.easynotes.presentation.screens.edit.model


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kin.easynotes.domain.model.Note
import com.kin.easynotes.domain.usecase.NoteViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking


class EditViewModel : NoteViewModel() {
    private val _noteNameState = mutableStateOf(TextFieldValue(""))
    val noteNameState: State<TextFieldValue> get() = _noteNameState

    private val _noteDescriptionState = mutableStateOf(TextFieldValue(""))
    val noteDescriptionState: State<TextFieldValue> get() = _noteDescriptionState

    private var _noteInfoState: MutableState<Boolean> = mutableStateOf(false)
    val noteInfoState: State<Boolean> get() = _noteInfoState

    override fun getNoteById(id: Int): Flow<Note> {
        return when (id) {
            0 -> flowOf(Note(0,"",""))
            else -> super.getNoteById(id)
        }
    }

    fun syncNote(note: Note) {
        updateNoteNameState(TextFieldValue(note.name))
        updateNoteDescriptionState(TextFieldValue(note.description))
    }

    fun updateInfo(value: Boolean) {
        _noteInfoState.value = value
    }

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
