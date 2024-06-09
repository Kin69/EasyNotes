package com.kin.easynotes.data.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class Preferences(context: Context) {

    private val instance: SharedPreferences = context.getSharedPreferences("notes", Context.MODE_PRIVATE)

    var automaticTheme: Boolean
        get() = instance.getBoolean("automatic_theme", true)
        set(value) = instance.edit { putBoolean("automatic_theme", value) }

    var darkTheme: Boolean
        get() = instance.getBoolean("dark_theme", false)
        set(value) = instance.edit { putBoolean("dark_theme", value) }

    var dynamicTheme: Boolean
        get() = instance.getBoolean("dynamic_theme", false)
        set(value) = instance.edit { putBoolean("dynamic_theme", value) }

    var amoledTheme: Boolean
        get() = instance.getBoolean("amoled_theme", false)
        set(value) = instance.edit { putBoolean("amoled_theme", value) }

    var sortProperty: String
        get() = instance.getString("sort_property", "CREATED_DATE") ?: "CREATED_DATE"
        set(value) = instance.edit { putString("sort_property", value) }

    var sortOrder: String
        get() = instance.getString("sort_order", "ASCENDING") ?: "ASCENDING"
        set(value) = instance.edit { putString("sort_order", value) }

    fun edit(action: SharedPreferences.Editor.() -> Unit) = instance.edit(true, action)
}