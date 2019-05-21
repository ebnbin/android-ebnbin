package com.ebnbin.windowcamera.profile.fragment

import android.os.Bundle
import androidx.preference.Preference
import com.ebnbin.eb.preference.FooterPreference
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.profile.ProfileHelper
import com.ebnbin.windowcamera.profile.enumeration.Profile
import com.ebnbin.windowcamera.util.IOHelper

class OtherProfileFragment : BaseProfileFragment() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)
        Preference(preferenceScreen.context).apply {
            buildPreference(this, ProfileHelper.path)
            setTitle(R.string.profile_path_title)
            summary = IOHelper.getPath().toString()
            setIcon(R.drawable.profile_path)
        }

        Preference(preferenceScreen.context).apply {
            preferenceScreen.addPreference(this)
            val helpId = when (Profile.get(ProfileHelper.profile.value)) {
                Profile.DEFAULT -> R.string.profile_default_help
                Profile.WALKING -> R.string.profile_walking_help
                Profile.MIRROR -> R.string.profile_mirror_help
                Profile.CUSTOM_1 -> R.string.profile_custom_1_help
                Profile.CUSTOM_2 -> R.string.profile_custom_2_help
            }
            setSummary(helpId)
            setIcon(R.drawable.profile_help)
        }

        FooterPreference(preferenceScreen.context).apply {
            preferenceScreen.addPreference(this)
        }
    }
}
