package com.ebnbin.eb.app2

import android.app.Application
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.crashlytics.android.Crashlytics
import com.ebnbin.eb.BuildConfig
import com.ebnbin.eb.app2.crash.CrashActivity
import com.ebnbin.eb.app2.dev.DevFragment
import com.ebnbin.eb.app2.library.Libraries
import com.ebnbin.eb.app2.util.DeviceUtil

/**
 * 基础 Application.
 */
open class EBApp : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        initFirebaseAnalytics()
        initCrashlytics()
        initCustomActivityOnCrash()
    }

    private fun initFirebaseAnalytics() {
        if (BuildConfig.DEBUG) return
        Libraries.firebaseAnalytics.setUserId(DeviceUtil.DEVICE_ID)
    }

    private fun initCrashlytics() {
        if (BuildConfig.DEBUG) return
        Crashlytics.setUserIdentifier(DeviceUtil.DEVICE_ID)
    }

    private fun initCustomActivityOnCrash() {
        CaocConfig.Builder.create()
            .enabled(true) // 始终启用.
            .errorActivity(CrashActivity::class.java)
            .apply()
    }

    open fun createAppReport(): Any? {
        return null
    }

    /**
     * Dev 页面.
     */
    open val devFragmentClass: Class<out DevFragment>
        get() = DevFragment::class.java

    companion object {
        lateinit var instance: EBApp
            private set
    }
}
