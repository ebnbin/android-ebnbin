package com.ebnbin.windowcamera.profile.fragment

import android.os.Bundle
import com.ebnbin.eb.app.EBPreferenceFragment
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.profile.ProfileHelper

abstract class BaseProfileFragment : EBPreferenceFragment() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = ProfileHelper.sharedPreferencesName

        setPreferencesFromResource(R.xml.base_profile_fragment, rootKey)
    }
}
