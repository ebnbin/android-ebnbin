package com.ebnbin.eb

import android.app.Application

open class EBApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: EBApplication
            private set
    }
}
