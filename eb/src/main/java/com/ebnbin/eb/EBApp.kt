package com.ebnbin.eb

import android.app.Application
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.ebnbin.eb.crash.CrashActivity
import com.ebnbin.eb.dev.DevFragment

/**
 * 基础 Application.
 */
open class EBApp : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        initCustomActivityOnCrash()
    }

    private fun initCustomActivityOnCrash() {
        CaocConfig.Builder.create()
            .enabled(true) // 始终启用.
            .errorActivity(CrashActivity::class.java)
            .apply()
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
