package com.ebnbin.eb.dev

import android.app.Application

object EBDev {
    internal lateinit var app: Application
        private set

    fun init(app: Application) {
        this.app = app
        this.app.registerActivityLifecycleCallbacks(DevFloatingActivityLifecycleCallbacks)
    }
}
