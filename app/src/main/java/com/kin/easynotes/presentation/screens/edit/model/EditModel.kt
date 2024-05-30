package com.kin.easynotes.presentation.screens.edit.model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.kin.easynotes.domain.model.Note
import com.kin.easynotes.domain.usecase.NoteViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class EditViewModel : NoteViewModel() {
    private val _noteName = mutableStateOf(TextFieldValue())
    val noteName: State<TextFieldValue> get() = _noteName

    private val _noteDescription = mutableStateOf(TextFieldValue())
    val noteDescription: State<TextFieldValue> get() = _noteDescription

    private val _noteCreatedTime = mutableLongStateOf(0)
    val noteCreatedTime: State<Long> get() = _noteCreatedTime

    private val _isNoteInfoVisible = mutableStateOf(false)
    val isNoteInfoVisible: State<Boolean> get() = _isNoteInfoVisible

    override fun getNoteById(id: Int): Flow<Note> {
        return when (id) {
            0 -> flowOf(Note(0, "", "",0L))
            else -> super.getNoteById(id)
        }
    }

    fun saveNote(id: Int) {
        if (noteName.value.text.isNotEmpty() || noteDescription.value.text.isNotEmpty()) {
            if (noteName.value.text.isEmpty()) {
                updateNoteName(TextFieldValue("Empty"))
            }
            if (noteDescription.value.text.isEmpty()) {
                updateNoteDescription(TextFieldValue("Empty"))
            }

            when (id) {
                0 -> addNote(Note(name = noteName.value.text, description = noteDescription.value.text))
                else -> updateNote(Note(id = id, name = noteName.value.text, description = noteDescription.value.text))
            }
        }
    }

    fun syncNote(note: Note) {
        updateNoteName(TextFieldValue(note.name))
        updateNoteDescription(TextFieldValue(note.description))
        updateNoteCreatedTime(note.createdAt)
    }

    fun toggleNoteInfoVisibility(value: Boolean) {
        _isNoteInfoVisible.value = value
    }

    fun updateNoteName(newName: TextFieldValue) {
        _noteName.value = newName
    }

    private fun updateNoteCreatedTime(newTime: Long) {
        _noteCreatedTime.longValue = newTime
    }

    fun updateNoteDescription(newDescription: TextFieldValue) {
        _noteDescription.value = newDescription
    }

    fun insertText(insertText: String, offset: Int = 1) {
        val text = _noteDescription.value.text.let {
            if (it.isNotEmpty()) "$it\n" else it
        }
        _noteDescription.value = TextFieldValue(
            text = "$text$insertText",
            selection = TextRange(text.length + insertText.length + offset)
        )
    }
}
