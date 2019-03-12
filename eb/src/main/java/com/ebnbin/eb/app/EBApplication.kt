package com.ebnbin.eb.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.ebnbin.eb.crash.CrashActivity
import com.ebnbin.eb.sharedpreferences.EBSp

/**
 * Base Application.
 */
open class EBApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        CaocConfig.Builder.create()
            .errorActivity(CrashActivity::class.java)
            .trackActivities(true)
            .apply()
        AppCompatDelegate.setDefaultNightMode(EBSp.eb.night_mode)
    }

    /**
     * Application 配置.
     */
    open val config: EBApplicationConfig = EBApplicationConfig()

    companion object {
        internal lateinit var instance: EBApplication
            private set
    }
}
