package com.kin.easynotes.domain.repository

interface SettingsRepository {
    suspend fun putString(key: String, value: String)
    suspend fun getString(key: String): String?

    suspend fun putInt(key: String, value: Int)
    suspend fun getInt(key: String): Int?

    suspend fun putBoolean(key: String, value: Boolean)
    suspend fun getBoolean(key: String): Boolean?

    suspend fun getEveryNotesWidget(): List<Pair<Int, Int>>
}
