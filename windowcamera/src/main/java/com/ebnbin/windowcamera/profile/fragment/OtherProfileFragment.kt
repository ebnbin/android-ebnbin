package com.ebnbin.windowcamera.profile.fragment

import android.os.Bundle
import androidx.preference.Preference
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.preference.FooterPreference
import com.ebnbin.windowcamera.profile.ProfileHelper

class OtherProfileFragment : BaseProfileFragment() {
    private lateinit var pathPreference: Preference
    private lateinit var footerPreference: FooterPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)

        pathPreference = Preference(requireContext()).apply {
            key = ProfileHelper.KEY_PATH
            setTitle(R.string.profile_path)
            setSummary(R.string.profile_path_summary)
            preferenceScreen.addPreference(this)
        }

        footerPreference = FooterPreference(requireContext()).apply {
            preferenceScreen.addPreference(this)
        }
    }
}
