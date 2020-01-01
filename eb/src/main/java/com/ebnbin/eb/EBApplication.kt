package com.ebnbin.eb

import android.app.Application
import com.ebnbin.eb.dev.DevFloatingActivityLifecycleCallbacks

open class EBApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        initDev()
    }

    private fun initDev() {
        registerActivityLifecycleCallbacks(DevFloatingActivityLifecycleCallbacks)
    }

    companion object {
        lateinit var instance: EBApplication
            private set
    }
}
