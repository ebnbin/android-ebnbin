package com.ebnbin.eb

import android.app.Application
import com.ebnbin.eb.dev.EBDevFragment

/**
 * 基础 Application.
 */
open class EBApp : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    /**
     * Dev 页面.
     */
    open val devFragmentClass: Class<out EBDevFragment> = EBDevFragment::class.java

    companion object {
        lateinit var instance: EBApp
            private set
    }
}
