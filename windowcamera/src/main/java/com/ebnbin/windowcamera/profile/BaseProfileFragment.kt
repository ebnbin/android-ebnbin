package com.ebnbin.windowcamera.profile

import android.os.Bundle
import com.ebnbin.eb.app.EBPreferenceFragment
import com.ebnbin.windowcamera.R

abstract class BaseProfileFragment : EBPreferenceFragment() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.base_profile_fragment, rootKey)
    }
}
