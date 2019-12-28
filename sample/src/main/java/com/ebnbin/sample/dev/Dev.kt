package com.ebnbin.sample.dev

import android.app.Application
import com.ebnbin.eb.dev.EBDev
import com.ebnbin.eb.preference.Preference

object Dev {
    fun init(app: Application) {
        EBDev.init(app) {
            Preference(requireContext()).also {
                preferenceScreen.addPreference(it)
                it.title = "Sample"
            }
        }
    }
}
