package com.ebnbin.windowcamera.profile

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.SeekBarPreference
import com.ebnbin.windowcamera.R

class WindowProfileFragment : BaseProfileFragment() {
    private lateinit var sizePreference: SeekBarPreference
    private lateinit var ratioPreference: ListPreference

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
            preferenceScreen.addPreference(this)
        }

        ratioPreference = ListPreference(requireContext()).apply {
            key = ProfileHelper.KEY_RATIO
            setDefaultValue(ProfileHelper.DEF_VALUE_RATIO)
            setTitle(R.string.profile_ratio)
            summaryProvider = Preference.SummaryProvider<ListPreference> {
                when(it.value) {
                    "capture" -> getString(R.string.profile_ratio_summary_capture)
                    "raw" -> getString(R.string.profile_ratio_summary_raw)
                    "screen" -> getString(R.string.profile_ratio_summary_screen)
                    "square" -> getString(R.string.profile_ratio_summary_square)
                    else -> ""
                }
            }
            entries = arrayOf(
                getString(R.string.profile_ratio_summary_capture),
                getString(R.string.profile_ratio_summary_raw),
                getString(R.string.profile_ratio_summary_screen),
                getString(R.string.profile_ratio_summary_square)
            )
            entryValues = arrayOf(
                "capture",
                "raw",
                "screen",
                "square"
            )
            setDialogTitle(R.string.profile_ratio)
            preferenceScreen.addPreference(this)
        }
    }
}
