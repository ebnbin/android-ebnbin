package com.ebnbin.eb2

import androidx.appcompat.app.AppCompatDelegate
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.crashlytics.android.Crashlytics
import com.ebnbin.eb.EBApp
import com.ebnbin.eb2.crash.CrashActivity
import com.ebnbin.eb2.debug.debug
import com.ebnbin.eb2.sharedpreferences.EBSpManager
import com.ebnbin.eb2.util.DeviceHelper

/**
 * Base Application.
 */
open class EBApplication : EBApp() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        initCaoc()
        initCrashlytics()
        initNightMode()
    }

    private fun initCaoc() {
        CaocConfig.Builder.create()
            .enabled(debug)
            .errorActivity(CrashActivity::class.java)
            .apply()
    }

    private fun initCrashlytics() {
        if (debug) return
        Crashlytics.setUserIdentifier(DeviceHelper.DEVICE_ID)
    }

    private fun initNightMode() {
        AppCompatDelegate.setDefaultNightMode(EBSpManager.night_mode.value)
    }

    companion object {
        internal lateinit var instance: EBApplication
            private set
    }
}
