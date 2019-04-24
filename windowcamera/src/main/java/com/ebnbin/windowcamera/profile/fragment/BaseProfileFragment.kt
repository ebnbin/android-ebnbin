package com.ebnbin.windowcamera.profile.fragment

import android.os.Bundle
import com.ebnbin.eb.app.EBPreferenceFragment
import com.ebnbin.eb.sharedpreferences.SharedPreferencesHelper
import com.ebnbin.windowcamera.profile.ProfileHelper

abstract class BaseProfileFragment : EBPreferenceFragment() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName =
            SharedPreferencesHelper.getSharedPreferencesName(ProfileHelper.sharedPreferencesNamePostfix)
    }
}
