package com.ebnbin.windowcamera.profile

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.ebnbin.windowcamera.R

abstract class BaseProfileFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.base_profile_fragment, rootKey)
    }
}
