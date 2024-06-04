package com.kin.easynotes

import android.app.Application
import com.kin.easynotes.di.DataModule
import com.kin.easynotes.di.DataModuleImpl

class Notes : Application() {
    companion object {
        lateinit var dataModule: DataModule
    }

    override fun onCreate() {
        super.onCreate()
        dataModule = DataModuleImpl(this)
    }
}