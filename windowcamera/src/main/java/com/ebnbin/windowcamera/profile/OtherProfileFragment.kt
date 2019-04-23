package com.ebnbin.windowcamera.profile

import android.os.Bundle
import androidx.preference.Preference
import com.ebnbin.windowcamera.R

class OtherProfileFragment : BaseProfileFragment() {
    private lateinit var pathPreference: Preference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)

        pathPreference = Preference(requireContext()).apply {
            key = ProfileHelper.KEY_PATH
            setTitle(R.string.profile_path)
            setSummary(R.string.profile_path_summary)
            preferenceScreen.addPreference(this)
        }
    }
}
