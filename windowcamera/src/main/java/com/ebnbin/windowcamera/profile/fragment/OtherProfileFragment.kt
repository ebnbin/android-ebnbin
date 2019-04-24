package com.ebnbin.windowcamera.profile.fragment

import android.os.Bundle
import android.os.Environment
import androidx.preference.Preference
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.profile.ProfileSpManager

class OtherProfileFragment : BaseProfileFragment() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)
        setPreferencesFromResource(R.xml.other_profile_fragment, rootKey)

        findPreference<Preference>(ProfileSpManager.path.key)?.run {
            summary = getString(R.string.profile_path_summary, context.getExternalFilesDir(Environment.DIRECTORY_DCIM))
        }
    }
}
