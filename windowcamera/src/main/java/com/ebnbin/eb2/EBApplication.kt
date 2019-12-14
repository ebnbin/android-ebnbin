package com.ebnbin.eb2

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.crashlytics.android.Crashlytics
import com.ebnbin.eb2.crash.CrashActivity
import com.ebnbin.eb2.debug.BaseDebugPageFragment
import com.ebnbin.eb2.debug.debug
import com.ebnbin.eb2.sharedpreferences.EBSpManager
import com.ebnbin.eb2.util.DeviceHelper

/**
 * Base Application.
 */
open class EBApplication : Application() {
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

    /**
     * 应用 debug page 页面.
     */
    open val debugPageFragmentClass: Class<out BaseDebugPageFragment>? = null

    companion object {
        internal lateinit var instance: EBApplication
            private set
    }
}
