package com.ebnbin.windowcamera.profile.fragment

import android.os.Bundle
import android.os.Environment
import androidx.preference.Preference
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.profile.ProfileHelper

class OtherProfileFragment : BaseProfileFragment() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)
        setPreferencesFromResource(R.xml.profile_other_fragment, rootKey)

        findPreference<Preference>(ProfileHelper.path.key)?.run {
            summary = getString(R.string.profile_path_summary, context.getExternalFilesDir(Environment.DIRECTORY_DCIM))
        }
    }
}
