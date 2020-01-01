package com.ebnbin.sample.dev

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

internal class SampleDevPageFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceScreen = preferenceManager.createPreferenceScreen(requireContext())
    }
}
