package com.kin.easynotes.domain.model

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme

data class Settings(
    val automaticTheme: Boolean = false,
    val darkTheme: Boolean = false,
    val dynamicTheme: Boolean = false,
    var amoledTheme: Boolean = false
)
