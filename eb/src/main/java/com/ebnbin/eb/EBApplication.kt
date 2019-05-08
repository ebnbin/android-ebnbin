package com.ebnbin.eb

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.crashlytics.android.Crashlytics
import com.ebnbin.eb.crash.CrashActivity
import com.ebnbin.eb.debug.BaseDebugPageFragment
import com.ebnbin.eb.debug.debug
import com.ebnbin.eb.sharedpreferences.EBSpManager
import com.ebnbin.eb.util.DeviceHelper

/**
 * Base Application.
 */
open class EBApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        CaocConfig.Builder.create()
            .errorActivity(CrashActivity::class.java)
            .apply()
        if (!debug) {
            Crashlytics.setUserIdentifier(DeviceHelper.DEVICE_ID)
        }
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
