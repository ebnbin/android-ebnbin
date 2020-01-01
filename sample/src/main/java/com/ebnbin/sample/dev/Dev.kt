package com.ebnbin.sample.dev

import android.app.Application
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.ebnbin.eb.dev.EBDev
import com.ebnbin.eb.fragment.openFragment
import com.ebnbin.eb.preference.Preference

object Dev {
    fun init(app: Application) {
        EBDev.init(app) {
            Preference(requireContext()).also {
                preferenceScreen.addPreference(it)
                it.title = "Sample"
                it.setOnPreferenceClickListener {
                    openFragment(Class.forName("com.ebnbin.eb.app2.dev.DevFragment2") as Class<out Fragment>, bundleOf("calling_activity" to "test"))
                    true
                }
            }
        }
    }
}
