package com.ebnbin.windowcamera.profile.fragment

import android.os.Bundle
import android.os.Environment
import androidx.preference.Preference
import com.ebnbin.eb.preference.FooterPreference
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.profile.ProfileSpManager

class OtherProfileFragment : BaseProfileFragment() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)

        Preference(preferenceScreen.context).run {
            preferenceScreen.addPreference(this)
            key = ProfileSpManager.path.key
            setTitle(R.string.profile_path)
            summary = getString(R.string.profile_path_summary, context.getExternalFilesDir(Environment.DIRECTORY_DCIM))
            setIcon(R.drawable.profile_path)
        }

        FooterPreference(preferenceScreen.context).run {
            preferenceScreen.addPreference(this)
        }
    }
}
