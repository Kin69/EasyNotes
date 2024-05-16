package com.kin.easynotes

import android.app.Application
import com.kin.easynotes.domain.holder.DatabaseHolder
import com.kin.easynotes.domain.usecase.Preferences

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initializeDependencies()
    }

    private fun initializeDependencies() {
        DatabaseHolder.provide(this)
        Preferences.init(this)
    }
}