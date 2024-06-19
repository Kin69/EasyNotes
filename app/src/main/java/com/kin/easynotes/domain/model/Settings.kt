package com.kin.easynotes.domain.model

data class Settings(
    val viewMode: Int = 0,
    val automaticTheme: Boolean = true,
    val darkTheme: Boolean = false,
    var dynamicTheme: Boolean = false,
    var amoledTheme: Boolean = false
)

