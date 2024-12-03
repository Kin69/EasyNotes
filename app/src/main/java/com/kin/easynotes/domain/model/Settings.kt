package com.kin.easynotes.domain.model

data class Settings(
    var passcode: String? = null,
    var fingerprint: Boolean = false,
    var pattern: String? = null,
    val viewMode: Boolean = true,
    val automaticTheme: Boolean = true,
    val darkTheme: Boolean = false,
    var dynamicTheme: Boolean = false,
    var amoledTheme: Boolean = false,
    var minimalisticMode: Boolean = false,
    var extremeAmoledMode: Boolean = false,
    var isMarkdownEnabled: Boolean = true,
    var screenProtection: Boolean = false,
    var encryptBackup: Boolean = false,
    var sortDescending: Boolean = true,
    var vaultSettingEnabled: Boolean = false,
    var vaultEnabled: Boolean = false,
    var editMode: Boolean = false,
    var gallerySync: Boolean = true,
    var showOnlyTitle: Boolean = false,
    var termsOfService: Boolean = false,
    var useMonoSpaceFont: Boolean = false,
    var lockImmediately: Boolean = true,

    var cornerRadius: Int = 32,
)

