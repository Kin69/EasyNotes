package com.kin.easynotes.presentation.screens.search.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import com.kin.easynotes.domain.repository.NoteRepository
import com.kin.easynotes.presentation.screens.home.viewmodel.HomeViewModel

class SearchViewModel(
    private val noteRepository: NoteRepository
) : HomeViewModel(noteRepository) {

    private var _value = mutableStateOf(TextFieldValue(""))
    val value: State<TextFieldValue> get() = _value

    fun updateValue(value: TextFieldValue) {
        _value.value = value
    }
}