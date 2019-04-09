package com.ebnbin.windowcamera.profile

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.CheckBoxPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.SeekBarPreference
import com.ebnbin.eb.sharedpreferences.get
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.profile.preference.EmptyPreferenceGroup
import com.ebnbin.windowcamera.profile.preference.FooterPreference

class WindowProfileFragment : BaseProfileFragment(), SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroy() {
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        super.onDestroy()
    }

    private lateinit var layoutPreference: PreferenceCategory
    private lateinit var sizePreference: SeekBarPreference
    private lateinit var ratioPreference: ListPreference
    private lateinit var isOutEnabledPreference: CheckBoxPreference
    private lateinit var isOutEnabledOffPreferenceGroup: EmptyPreferenceGroup
    private lateinit var inXPreference: SeekBarPreference
    private lateinit var inYPreference: SeekBarPreference
    private lateinit var isOutEnabledOnPreferenceGroup: EmptyPreferenceGroup
    private lateinit var outXPreference: SeekBarPreference
    private lateinit var outYPreference: SeekBarPreference
    private lateinit var footerPreference: FooterPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)
        layoutPreference = PreferenceCategory(requireContext()).apply {
            key = ProfileHelper.KEY_LAYOUT
            setTitle(R.string.profile_layout)
            preferenceScreen.addPreference(this)
        }

        sizePreference = SeekBarPreference(requireContext()).apply {
            key = ProfileHelper.KEY_SIZE
            setDefaultValue(ProfileHelper.DEF_VALUE_SIZE)
            setTitle(R.string.profile_size)
            setSummary(R.string.profile_size_summary)
            min = 1
            max = 100
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
            isOutEnabledOffPreferenceGroup.addPreference(this)
        }

        inYPreference = SeekBarPreference(requireContext()).apply {
            key = ProfileHelper.KEY_IN_Y
            setDefaultValue(ProfileHelper.DEF_VALUE_IN_Y)
            setTitle(R.string.profile_in_y)
            setSummary(R.string.profile_in_y_summary)
            min = 0
            max = 100
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
            min = -99
            max = 199
            isOutEnabledOnPreferenceGroup.addPreference(this)
        }

        outYPreference = SeekBarPreference(requireContext()).apply {
            key = ProfileHelper.KEY_OUT_Y
            setDefaultValue(ProfileHelper.DEF_VALUE_OUT_Y)
            setTitle(R.string.profile_out_y)
            setSummary(R.string.profile_out_y_summary)
            min = -99
            max = 199
            isOutEnabledOnPreferenceGroup.addPreference(this)
        }

        footerPreference = FooterPreference(requireContext()).apply {
            preferenceScreen.addPreference(this)
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        sharedPreferences ?: return
        when (key) {
            ProfileHelper.KEY_OUT_X -> {
                val oldValue = outXPreference.value
                val newValue = sharedPreferences.get(ProfileHelper.KEY_OUT_X, ProfileHelper.DEF_VALUE_OUT_X)
                if (oldValue != newValue) {
                    outXPreference.value = newValue
                }
            }
            ProfileHelper.KEY_OUT_Y -> {
                val oldValue = outYPreference.value
                val newValue = sharedPreferences.get(ProfileHelper.KEY_OUT_Y, ProfileHelper.DEF_VALUE_OUT_Y)
                if (oldValue != newValue) {
                    outYPreference.value = newValue
                }
            }
            ProfileHelper.KEY_IN_X -> {
                val oldValue = inXPreference.value
                val newValue = sharedPreferences.get(ProfileHelper.KEY_IN_X, ProfileHelper.DEF_VALUE_IN_X)
                if (oldValue != newValue) {
                    inXPreference.value = newValue
                }
            }
            ProfileHelper.KEY_IN_Y -> {
                val oldValue = inYPreference.value
                val newValue = sharedPreferences.get(ProfileHelper.KEY_IN_Y, ProfileHelper.DEF_VALUE_IN_Y)
                if (oldValue != newValue) {
                    inYPreference.value = newValue
                }
            }
        }
    }
}
