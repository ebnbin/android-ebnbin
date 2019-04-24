package com.ebnbin.windowcamera.profile.fragment

import android.os.Bundle
import androidx.preference.CheckBoxPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceGroup
import androidx.preference.SeekBarPreference
import com.ebnbin.eb.preference.FooterPreference
import com.ebnbin.eb.preference.SimplePreferenceGroup
import com.ebnbin.eb.preference.SimpleSeekBarPreference
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.profile.ProfileSpManager

class WindowProfileFragment : BaseProfileFragment() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)
        PreferenceCategory(preferenceScreen.context).run {
            key = ProfileSpManager.layout.key
            preferenceScreen.addPreference(this)
            setTitle(R.string.profile_layout)
        }

        SeekBarPreference(preferenceScreen.context).run {
            key = ProfileSpManager.size.key
            preferenceScreen.addPreference(this)
            setDefaultValue(ProfileSpManager.size.getDefaultValue())
            setTitle(R.string.profile_size)
            setSummary(R.string.profile_size_summary)
            min = 1
            max = 100
            setIcon(R.drawable.profile_size)
        }

        ListPreference(preferenceScreen.context).run {
            key = ProfileSpManager.ratio.key
            preferenceScreen.addPreference(this)
            setDefaultValue(ProfileSpManager.ratio.getDefaultValue())
            setTitle(R.string.profile_ratio)
            summaryProvider = Preference.SummaryProvider<ListPreference> { entry }
            setIcon(R.drawable.profile_ratio)
            setDialogIcon(R.drawable.profile_ratio)
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
        }

        CheckBoxPreference(preferenceScreen.context).run {
            key = ProfileSpManager.is_out_enabled.key
            preferenceScreen.addPreference(this)
            setDefaultValue(ProfileSpManager.is_out_enabled.getDefaultValue())
            setTitle(R.string.profile_is_out_enabled)
            setSummaryOff(R.string.profile_is_out_enabled_summary_off)
            setSummaryOn(R.string.profile_is_out_enabled_summary_on)
            setIcon(R.drawable.profile_is_out_enabled)
        }

        SimplePreferenceGroup(preferenceScreen.context).run {
            key = ProfileSpManager.is_out_enabled_off.key
            preferenceScreen.addPreference(this)
            visibleKeys = Pair(setOf(ProfileSpManager.is_out_enabled.key), null)
        }

        SimpleSeekBarPreference(preferenceScreen.context).run {
            key = ProfileSpManager.in_x.key
            findPreference<PreferenceGroup>(ProfileSpManager.is_out_enabled_off.key)?.addPreference(this)
            setDefaultValue(ProfileSpManager.in_x.getDefaultValue())
            setTitle(R.string.profile_in_x)
            setSummary(R.string.profile_in_x_summary)
            min = 0
            max = 100
            setIcon(R.drawable.profile_x)
        }

        SimpleSeekBarPreference(preferenceScreen.context).run {
            key = ProfileSpManager.in_y.key
            findPreference<PreferenceGroup>(ProfileSpManager.is_out_enabled_off.key)?.addPreference(this)
            setDefaultValue(ProfileSpManager.in_y.getDefaultValue())
            setTitle(R.string.profile_in_y)
            setSummary(R.string.profile_in_y_summary)
            min = 0
            max = 100
            setIcon(R.drawable.profile_y)
        }

        SimplePreferenceGroup(preferenceScreen.context).run {
            key = ProfileSpManager.is_out_enabled_on.key
            preferenceScreen.addPreference(this)
            visibleKeys = Pair(null, setOf(ProfileSpManager.is_out_enabled.key))
        }

        SimpleSeekBarPreference(preferenceScreen.context).run {
            key = ProfileSpManager.out_x.key
            findPreference<PreferenceGroup>(ProfileSpManager.is_out_enabled_on.key)?.addPreference(this)
            setDefaultValue(ProfileSpManager.out_x.getDefaultValue())
            setTitle(R.string.profile_out_x)
            setSummary(R.string.profile_out_x_summary)
            min = -99
            max = 199
            setIcon(R.drawable.profile_x)
        }

        SimpleSeekBarPreference(preferenceScreen.context).run {
            key = ProfileSpManager.out_y.key
            findPreference<PreferenceGroup>(ProfileSpManager.is_out_enabled_on.key)?.addPreference(this)
            setDefaultValue(ProfileSpManager.out_y.getDefaultValue())
            setTitle(R.string.profile_out_y)
            setSummary(R.string.profile_out_y_summary)
            min = -99
            max = 199
            setIcon(R.drawable.profile_y)
        }

        PreferenceCategory(preferenceScreen.context).run {
            key = ProfileSpManager.display.key
            preferenceScreen.addPreference(this)
            setTitle(R.string.profile_display)
        }

        SeekBarPreference(preferenceScreen.context).run {
            key = ProfileSpManager.alpha.key
            preferenceScreen.addPreference(this)
            setDefaultValue(ProfileSpManager.alpha.getDefaultValue())
            setTitle(R.string.profile_alpha)
            min = 1
            max = 100
            setIcon(R.drawable.profile_alpha)
        }

        CheckBoxPreference(preferenceScreen.context).run {
            key = ProfileSpManager.is_keep_screen_on_enabled.key
            preferenceScreen.addPreference(this)
            setDefaultValue(ProfileSpManager.is_keep_screen_on_enabled.getDefaultValue())
            setTitle(R.string.profile_is_keep_screen_on_enabled)
            setSummaryOff(R.string.profile_is_keep_screen_on_enabled_summary_off)
            setSummaryOn(R.string.profile_is_keep_screen_on_enabled_summary_on)
            setIcon(R.drawable.profile_is_keep_screen_on_enabled)
        }

        PreferenceCategory(preferenceScreen.context).run {
            key = ProfileSpManager.control.key
            preferenceScreen.addPreference(this)
            setTitle(R.string.profile_control)
        }

        CheckBoxPreference(preferenceScreen.context).run {
            key = ProfileSpManager.is_touchable.key
            preferenceScreen.addPreference(this)
            setDefaultValue(ProfileSpManager.is_touchable.getDefaultValue())
            setTitle(R.string.profile_is_touchable)
            setSummaryOff(R.string.profile_is_touchable_summary_off)
            setSummaryOn(R.string.profile_is_touchable_summary_on)
            setIcon(R.drawable.profile_is_touchable)
        }

        SimplePreferenceGroup(preferenceScreen.context).run {
            key = ProfileSpManager.is_touchable_on.key
            preferenceScreen.addPreference(this)
            visibleKeys = Pair(null, setOf(ProfileSpManager.is_touchable.key))
        }

        Preference(preferenceScreen.context).run {
            key = ProfileSpManager.single_tap.key
            findPreference<PreferenceGroup>(ProfileSpManager.is_touchable_on.key)?.addPreference(this)
            setTitle(R.string.profile_single_tap)
            setSummary(R.string.profile_single_tap_summary)
            setIcon(R.drawable.profile_single_tap)
        }

        Preference(preferenceScreen.context).run {
            key = ProfileSpManager.double_tap.key
            findPreference<PreferenceGroup>(ProfileSpManager.is_touchable_on.key)?.addPreference(this)
            setTitle(R.string.profile_double_tap)
            setSummary(R.string.profile_double_tap_summary)
            setIcon(R.drawable.profile_double_tap)
        }

        Preference(preferenceScreen.context).run {
            key = ProfileSpManager.long_press.key
            findPreference<PreferenceGroup>(ProfileSpManager.is_touchable_on.key)?.addPreference(this)
            setTitle(R.string.profile_long_press)
            setSummary(R.string.profile_long_press_summary)
            setIcon(R.drawable.profile_long_press)
        }

        CheckBoxPreference(preferenceScreen.context).run {
            key = ProfileSpManager.is_move_enabled.key
            findPreference<PreferenceGroup>(ProfileSpManager.is_touchable_on.key)?.addPreference(this)
            setDefaultValue(ProfileSpManager.is_move_enabled.getDefaultValue())
            setTitle(R.string.profile_is_move_enabled)
            setSummaryOff(R.string.profile_is_move_enabled_summary_off)
            setSummaryOn(R.string.profile_is_move_enabled_summary_on)
            setIcon(R.drawable.profile_is_move_enabled)
        }

        FooterPreference(preferenceScreen.context).run {
            preferenceScreen.addPreference(this)
        }
    }
}
