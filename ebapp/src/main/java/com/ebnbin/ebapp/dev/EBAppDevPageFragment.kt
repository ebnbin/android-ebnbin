package com.ebnbin.ebapp.dev

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

internal class EBAppDevPageFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = DevSpManager.name
        preferenceScreen = preferenceManager.createPreferenceScreen(requireContext())
    }
}
