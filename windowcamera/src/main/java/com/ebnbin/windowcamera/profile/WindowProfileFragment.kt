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
    private lateinit var xPreference: SeekBarPreference
    private lateinit var yPreference: SeekBarPreference
    private lateinit var isOutEnabledPreference: CheckBoxPreference
    private lateinit var displayPreference: PreferenceCategory
    private lateinit var alphaPreference: SeekBarPreference
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

        xPreference = SeekBarPreference(requireContext()).apply {
            key = ProfileHelper.KEY_X
            setDefaultValue(ProfileHelper.DEF_VALUE_X)
            setTitle(R.string.profile_x)
            setSummary(R.string.profile_x_summary)
            min = -99
            max = 199
            preferenceScreen.addPreference(this)
        }

        yPreference = SeekBarPreference(requireContext()).apply {
            key = ProfileHelper.KEY_Y
            setDefaultValue(ProfileHelper.DEF_VALUE_Y)
            setTitle(R.string.profile_y)
            setSummary(R.string.profile_y_summary)
            min = -99
            max = 199
            preferenceScreen.addPreference(this)
        }

        isOutEnabledPreference = CheckBoxPreference(requireContext()).apply {
            key = ProfileHelper.KEY_IS_OUT_ENABLED
            setDefaultValue(ProfileHelper.DEF_VALUE_IS_OUT_ENABLED)
            setTitle(R.string.profile_is_out_enable)
            setSummaryOff(R.string.profile_is_out_enable_summary_off)
            setSummaryOn(R.string.profile_is_out_enable_summary_on)
            preferenceScreen.addPreference(this)
        }

        displayPreference = PreferenceCategory(requireContext()).apply {
            key = ProfileHelper.KEY_DISPLAY
            setTitle(R.string.profile_display)
            preferenceScreen.addPreference(this)
        }

        alphaPreference = SeekBarPreference(requireContext()).apply {
            key = ProfileHelper.KEY_ALPHA
            setDefaultValue(ProfileHelper.DEF_VALUE_ALPHA)
            setTitle(R.string.profile_alpha)
            setSummary(R.string.profile_alpha_summary)
            min = 1
            max = 100
            preferenceScreen.addPreference(this)
        }

        footerPreference = FooterPreference(requireContext()).apply {
            preferenceScreen.addPreference(this)
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        sharedPreferences ?: return
        when (key) {
            ProfileHelper.KEY_X -> {
                val oldValue = xPreference.value
                val newValue = sharedPreferences.get(ProfileHelper.KEY_X, ProfileHelper.DEF_VALUE_X)
                if (oldValue != newValue) {
                    xPreference.value = newValue
                }
            }
            ProfileHelper.KEY_Y -> {
                val oldValue = yPreference.value
                val newValue = sharedPreferences.get(ProfileHelper.KEY_Y, ProfileHelper.DEF_VALUE_Y)
                if (oldValue != newValue) {
                    yPreference.value = newValue
                }
            }
        }
    }
}
