package com.ebnbin.eb

import android.app.Application

/**
 * Base Application.
 */
open class EBApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        internal lateinit var instance: EBApplication
            private set
    }
}
