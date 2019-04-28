package com.ebnbin.windowcamera.profile.fragment

import android.os.Bundle
import com.ebnbin.eb.preference.EBPreferenceFragment
import com.ebnbin.eb.sharedpreferences.SharedPreferencesHelper
import com.ebnbin.windowcamera.profile.ProfileHelper

abstract class BaseProfileFragment : EBPreferenceFragment() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName =
            SharedPreferencesHelper.getName(ProfileHelper.SHARED_PREFERENCES_NAME_POSTFIX)
    }
}
