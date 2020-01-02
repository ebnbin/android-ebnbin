package com.ebnbin.eb.app2

import cat.ereza.customactivityoncrash.config.CaocConfig
import com.crashlytics.android.Crashlytics
import com.ebnbin.eb.BuildConfig
import com.ebnbin.eb.app2.crash.CrashActivity
import com.ebnbin.eb.app2.dev.DevFragment2
import com.ebnbin.ebapp.EBAppApplication
import com.ebnbin.ebapp.deviceId
import com.ebnbin.ebapp.library.firebaseAnalytics

/**
 * 基础 Application.
 */
open class EBApp : EBAppApplication() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        initFirebaseAnalytics()
        initCrashlytics()
        initCustomActivityOnCrash()
    }

    private fun initFirebaseAnalytics() {
        if (BuildConfig.DEBUG) return
        firebaseAnalytics.setUserId(deviceId)
    }

    private fun initCrashlytics() {
        if (BuildConfig.DEBUG) return
        Crashlytics.setUserIdentifier(deviceId)
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
    open val devFragmentClass: Class<out DevFragment2>
        get() = DevFragment2::class.java

    companion object {
        lateinit var instance: EBApp
            private set
    }
}
