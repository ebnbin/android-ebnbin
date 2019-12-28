package com.ebnbin.sample

import com.ebnbin.eb.app2.EBApp
import com.ebnbin.eb.dev.EBDev
import com.ebnbin.eb.preference.Preference

class App : EBApp() {
    override fun onCreate() {
        super.onCreate()
        EBDev.init(this) {
            Preference(requireContext()).also {
                preferenceScreen.addPreference(it)
                it.title = "Sample"
            }
        }
    }
}
