package com.ebnbin.eb

import android.app.Application
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.ebnbin.eb.crash.CrashActivity

/**
 * Base Application.
 */
open class EBApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        initCaoc()
    }

    private fun initCaoc() {
        CaocConfig.Builder.create()
            .enabled(BuildConfig.DEBUG)
            .errorActivity(CrashActivity::class.java)
            .apply()
    }

    companion object {
        internal lateinit var instance: EBApplication
            private set
    }
}
