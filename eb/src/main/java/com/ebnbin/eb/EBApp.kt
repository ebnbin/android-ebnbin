package com.ebnbin.eb

import android.app.Application

/**
 * 基础 Application.
 */
open class EBApp : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: EBApp
            private set
    }
}
