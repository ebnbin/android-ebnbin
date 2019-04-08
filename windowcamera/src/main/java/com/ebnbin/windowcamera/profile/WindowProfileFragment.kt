package com.ebnbin.windowcamera.profile

import android.os.Bundle
import androidx.preference.CheckBoxPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.SeekBarPreference
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.preference.EmptyPreferenceGroup

class WindowProfileFragment : BaseProfileFragment() {
    private lateinit var sizePreference: SeekBarPreference
    private lateinit var ratioPreference: ListPreference
    private lateinit var isOutEnabledPreference: CheckBoxPreference
    private lateinit var isOutEnabledOffPreferenceGroup: EmptyPreferenceGroup
    private lateinit var inXPreference: SeekBarPreference
    private lateinit var inYPreference: SeekBarPreference
    private lateinit var isOutEnabledOnPreferenceGroup: EmptyPreferenceGroup
    private lateinit var outXPreference: SeekBarPreference
    private lateinit var outYPreference: SeekBarPreference

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

        isOutEnabledPreference = CheckBoxPreference(requireContext()).apply {
            key = ProfileHelper.KEY_IS_OUT_ENABLED
            setDefaultValue(ProfileHelper.DEF_VALUE_IS_OUT_ENABLED)
            setTitle(R.string.profile_is_out_enable)
            setSummaryOff(R.string.profile_is_out_enable_summary_off)
            setSummaryOn(R.string.profile_is_out_enable_summary_on)
            setOnPreferenceChangeListener { _, newValue ->
                newValue as Boolean
                isOutEnabledOffPreferenceGroup.isVisible = !newValue
                isOutEnabledOnPreferenceGroup.isVisible = newValue
                true
            }
            preferenceScreen.addPreference(this)
        }

        isOutEnabledOffPreferenceGroup = EmptyPreferenceGroup(requireContext()).apply {
            key = ProfileHelper.KEY_IS_OUT_ENABLED_OFF
            isVisible = !isOutEnabledPreference.isChecked
            preferenceScreen.addPreference(this)
        }

        inXPreference = SeekBarPreference(requireContext()).apply {
            key = ProfileHelper.KEY_IN_X
            setDefaultValue(ProfileHelper.DEF_VALUE_IN_X)
            setTitle(R.string.profile_in_x)
            setSummary(R.string.profile_in_x_summary)
            min = 0
            max = 100
            updatesContinuously = true
            showSeekBarValue = true
            isOutEnabledOffPreferenceGroup.addPreference(this)
        }

        inYPreference = SeekBarPreference(requireContext()).apply {
            key = ProfileHelper.KEY_IN_Y
            setDefaultValue(ProfileHelper.DEF_VALUE_IN_Y)
            setTitle(R.string.profile_in_y)
            setSummary(R.string.profile_in_y_summary)
            min = 0
            max = 100
            updatesContinuously = true
            showSeekBarValue = true
            isOutEnabledOffPreferenceGroup.addPreference(this)
        }

        isOutEnabledOnPreferenceGroup = EmptyPreferenceGroup(requireContext()).apply {
            key = ProfileHelper.KEY_IS_OUT_ENABLED_ON
            isVisible = isOutEnabledPreference.isChecked
            preferenceScreen.addPreference(this)
        }

        outXPreference = SeekBarPreference(requireContext()).apply {
            key = ProfileHelper.KEY_OUT_X
            setDefaultValue(ProfileHelper.DEF_VALUE_OUT_X)
            setTitle(R.string.profile_out_x)
            setSummary(R.string.profile_out_x_summary)
            min = 0
            max = 100
            updatesContinuously = true
            showSeekBarValue = true
            isOutEnabledOnPreferenceGroup.addPreference(this)
        }

        outYPreference = SeekBarPreference(requireContext()).apply {
            key = ProfileHelper.KEY_OUT_Y
            setDefaultValue(ProfileHelper.DEF_VALUE_OUT_Y)
            setTitle(R.string.profile_out_y)
            setSummary(R.string.profile_out_y_summary)
            min = 0
            max = 100
            updatesContinuously = true
            showSeekBarValue = true
            isOutEnabledOnPreferenceGroup.addPreference(this)
        }
    }
}
