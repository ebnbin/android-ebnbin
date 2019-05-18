package com.ebnbin.windowcamera.profile.fragment

import android.os.Bundle
import com.ebnbin.eb.preference.FooterPreference
import com.ebnbin.eb.preference.SimplePreference
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.profile.ProfileHelper
import com.ebnbin.windowcamera.util.IOHelper

class OtherProfileFragment : BaseProfileFragment() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)
        SimplePreference(preferenceScreen.context).apply {
            buildPreference(this, ProfileHelper.path)
            setTitle(R.string.profile_path_title)
            summary = IOHelper.getPath().toString()
            setIcon(R.drawable.profile_path)
        }

        FooterPreference(preferenceScreen.context).apply {
            preferenceScreen.addPreference(this)
        }
    }
}
