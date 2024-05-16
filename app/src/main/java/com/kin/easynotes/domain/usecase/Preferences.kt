package com.kin.easynotes.domain.usecase

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object Preferences {
    lateinit var instance: SharedPreferences

    const val AUTOMATIC_THEME = true
    const val DARK_THEME = false
    const val DYNAMIC_THEME = false
    const val AMOLED_THEME = false
    fun init(context: Context) {
        instance = context.getSharedPreferences("notes", Context.MODE_PRIVATE)
    }
    fun edit(action: SharedPreferences.Editor.() -> Unit) = instance.edit(true, action)
}