package com.kin.easynotes.domain.model

data class Settings(
    val viewMode: Boolean = true,
    val automaticTheme: Boolean = true,
    val darkTheme: Boolean = false,
    var dynamicTheme: Boolean = false,
    var amoledTheme: Boolean = false,
    var minimalisticMode: Boolean = false,
    var extremeAmoledMode: Boolean = false,
    var isMarkdownEnabled: Boolean = true,

    var cornerRadius: Int = 32,
)

