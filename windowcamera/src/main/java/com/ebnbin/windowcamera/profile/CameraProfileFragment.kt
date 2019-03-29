package com.ebnbin.windowcamera.profile

import android.os.Bundle
import androidx.preference.SwitchPreferenceCompat
import com.ebnbin.windowcamera.R

class CameraProfileFragment : BaseProfileFragment() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)
        val isFrontPreference: SwitchPreferenceCompat = SwitchPreferenceCompat(requireContext()).apply {
            fun invalidateIcon(value: Boolean) {
                setIcon(if (value) R.drawable.profile_is_front_on else R.drawable.profile_is_front_off)
            }

            key = ProfileHelper.KEY_IS_FRONT
            setDefaultValue(ProfileHelper.DEF_VALUE_IS_FRONT)
            setTitle(R.string.profile_is_front)
            setSummaryOff(R.string.profile_is_front_summary_off)
            setSummaryOn(R.string.profile_is_front_summary_on)
            setOnPreferenceChangeListener { _, newValue ->
                newValue as Boolean
                invalidateIcon(newValue)
                true
            }
            invalidateIcon(isChecked)
        }
        preferenceScreen.addPreference(isFrontPreference)
    }
}
