package com.ebnbin.eb.dev

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.ebnbin.eb.preference.Preference
import com.ebnbin.eb.preference.PreferenceCategory

internal class DevPreferenceFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = DevSpManager.name
        preferenceScreen = preferenceManager.createPreferenceScreen(requireContext())

        PreferenceCategory(requireContext()).also {
            preferenceScreen.addPreference(it)
            it.title = "EB"
        }

        Preference(requireContext()).also {
            preferenceScreen.addPreference(it)
            it.title = "Calling Activity"
            it.summary = (parentFragment as DevFragment).callingActivity
        }
    }
}
