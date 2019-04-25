package com.ebnbin.windowcamera.profile.fragment

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import com.ebnbin.eb.preference.SimplePreferenceGroup
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.profile.ProfileSpManager

class WindowProfileFragment : BaseProfileFragment() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)
        setPreferencesFromResource(R.xml.profile_window_fragment, rootKey)

        findPreference<ListPreference>(ProfileSpManager.ratio.key)?.run {
            summaryProvider = Preference.SummaryProvider<ListPreference> { entry }
        }

        findPreference<SimplePreferenceGroup>(ProfileSpManager.is_out_enabled_off.key)?.run {
            visibleKeysOff = arrayOf(ProfileSpManager.is_out_enabled.key)
        }

        findPreference<SimplePreferenceGroup>(ProfileSpManager.is_out_enabled_on.key)?.run {
            visibleKeysOn = arrayOf(ProfileSpManager.is_out_enabled.key)
        }

        findPreference<SimplePreferenceGroup>(ProfileSpManager.is_touchable_on.key)?.run {
            visibleKeysOn = arrayOf(ProfileSpManager.is_touchable.key)
        }
    }
}
