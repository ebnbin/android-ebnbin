package com.ebnbin.windowcamera.profile.fragment

import android.os.Bundle
import android.os.Environment
import androidx.preference.Preference
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.preference.FooterPreference
import com.ebnbin.windowcamera.profile.ProfileSpManager

class OtherProfileFragment : BaseProfileFragment() {
    private lateinit var pathPreference: Preference
    private lateinit var footerPreference: FooterPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)

        pathPreference = Preference(requireContext()).apply {
            key = ProfileSpManager.path.key
            setTitle(R.string.profile_path)
            summary = getString(R.string.profile_path_summary, context.getExternalFilesDir(Environment.DIRECTORY_DCIM))
            preferenceScreen.addPreference(this)
        }

        footerPreference = FooterPreference(requireContext()).apply {
            preferenceScreen.addPreference(this)
        }
    }
}
