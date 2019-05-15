package com.ebnbin.windowcamera.profile.fragment

import android.os.Bundle
import androidx.core.text.HtmlCompat
import androidx.preference.CheckBoxPreference
import com.ebnbin.eb.preference.SimplePreferenceGroup
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.profile.ProfileHelper

class WindowProfileFragment : BaseProfileFragment() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)
        setPreferencesFromResource(R.xml.profile_window_fragment, rootKey)

        findPreference<SimplePreferenceGroup>(ProfileHelper.is_out_enabled_off.key)?.run {
            visibleKeysOff = arrayOf(ProfileHelper.is_out_enabled.key)
        }

        findPreference<SimplePreferenceGroup>(ProfileHelper.is_out_enabled_on.key)?.run {
            visibleKeysOn = arrayOf(ProfileHelper.is_out_enabled.key)
        }

        findPreference<CheckBoxPreference>(ProfileHelper.is_border_enabled.key)?.run {
            title = HtmlCompat.fromHtml(getString(R.string.profile_is_border_enabled_title),
                HtmlCompat.FROM_HTML_MODE_COMPACT)
        }

        findPreference<SimplePreferenceGroup>(ProfileHelper.is_touchable_on.key)?.run {
            visibleKeysOn = arrayOf(ProfileHelper.is_touchable.key)
        }
    }
}
