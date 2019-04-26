package com.ebnbin.eb.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.ebnbin.eb.crash.CrashActivity
import com.ebnbin.eb.debug.BaseDebugPageFragment
import com.ebnbin.eb.sharedpreferences.EBSpManager

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
        AppCompatDelegate.setDefaultNightMode(EBSpManager.eb.night_mode.value)
    }

    open val mainActivityClass: Class<out EBActivity>? = null

    /**
     * 应用 debug page 页面.
     */
    open val debugPageFragmentClass: Class<out BaseDebugPageFragment>? = null

    companion object {
        internal lateinit var instance: EBApplication
            private set
    }
}
