package com.ebnbin.windowcamera.profile

import android.os.Bundle
import androidx.preference.SeekBarPreference
import com.ebnbin.windowcamera.R

class WindowProfileFragment : BaseProfileFragment() {
    private lateinit var sizePreference: SeekBarPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)
        sizePreference = SeekBarPreference(requireContext()).apply {
            key = ProfileHelper.KEY_SIZE
            setDefaultValue(ProfileHelper.DEF_VALUE_SIZE)
            setTitle(R.string.profile_size)
            setSummary(R.string.profile_size_summary)
            min = 1
            max = 100
            updatesContinuously = true
            showSeekBarValue = true
        }
        preferenceScreen.addPreference(sizePreference)
    }
}
