package com.ebnbin.eb2

import androidx.appcompat.app.AppCompatDelegate
import com.ebnbin.eb.app2.EBApp
import com.ebnbin.eb2.sharedpreferences.EBSpManager

/**
 * Base Application.
 */
open class EBApplication : EBApp() {
    override fun onCreate() {
        super.onCreate()
        initNightMode()
    }

    private fun initNightMode() {
        AppCompatDelegate.setDefaultNightMode(EBSpManager.night_mode.value)
    }
}
