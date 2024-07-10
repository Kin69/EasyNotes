package com.kin.easynotes.widget

import android.os.Bundle
import androidx.activity.ComponentActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class NotesWidgetActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val coroutineScope = MainScope()
        coroutineScope.launch {
            NotesWidgetReceiver.observeData(this@NotesWidgetActivity)
            finish()
        }
    }
}