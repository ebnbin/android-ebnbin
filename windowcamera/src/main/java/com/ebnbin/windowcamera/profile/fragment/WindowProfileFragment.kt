package com.ebnbin.windowcamera.profile.fragment

import android.os.Bundle
import androidx.core.text.HtmlCompat
import androidx.preference.CheckBoxPreference
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.SeekBarPreference
import com.ebnbin.eb.preference.FooterPreference
import com.ebnbin.eb.preference.SimpleListPreference
import com.ebnbin.eb.preference.SimplePreferenceGroup
import com.ebnbin.eb.preference.SimpleSeekBarPreference
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.profile.ProfileHelper

class WindowProfileFragment : BaseProfileFragment() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)
        PreferenceCategory(preferenceScreen.context).apply {
            buildPreference(this, ProfileHelper.layout)
            setTitle(R.string.profile_layout_title)
        }

        SeekBarPreference(preferenceScreen.context).apply {
            buildPreference(this, ProfileHelper.size, ProfileHelper.layout)
            setTitle(R.string.profile_size_title)
            setSummary(R.string.profile_size_summary)
            setIcon(R.drawable.profile_size)
            min = 1
            max = 100
        }

        SimpleListPreference(preferenceScreen.context).apply {
            buildPreference(this, ProfileHelper.ratio, ProfileHelper.layout)
            setTitle(R.string.profile_ratio_title)
            setIcon(R.drawable.profile_ratio)
            entryValues = arrayOf(
                "capture",
                "screen",
                "square"
            )
            setEntries(R.array.profile_ratio_entries)
            setDialogTitle(R.string.profile_ratio_title)
            setDialogIcon(R.drawable.profile_ratio)
        }

        CheckBoxPreference(preferenceScreen.context).apply {
            buildPreference(this, ProfileHelper.is_out_enabled, ProfileHelper.layout)
            setTitle(R.string.profile_is_out_enabled_title)
            setSummaryOff(R.string.profile_is_out_enabled_summary_off)
            setSummaryOn(R.string.profile_is_out_enabled_summary_on)
            setIcon(R.drawable.profile_is_out_enabled)
        }

        SimplePreferenceGroup(preferenceScreen.context).apply {
            buildPreference(this, ProfileHelper.is_out_enabled_off, ProfileHelper.layout)
            visibleKeysOff = arrayOf(ProfileHelper.is_out_enabled.key)
        }

        SimpleSeekBarPreference(preferenceScreen.context).apply {
            buildPreference(this, ProfileHelper.in_x, ProfileHelper.is_out_enabled_off)
            setTitle(R.string.profile_in_x_title)
            setSummary(R.string.profile_in_x_summary)
            setIcon(R.drawable.profile_x)
            min = 0
            max = 100
        }

        SimpleSeekBarPreference(preferenceScreen.context).apply {
            buildPreference(this, ProfileHelper.in_y, ProfileHelper.is_out_enabled_off)
            setTitle(R.string.profile_in_y_title)
            setSummary(R.string.profile_in_y_summary)
            setIcon(R.drawable.profile_y)
            min = 0
            max = 100
        }

        SimplePreferenceGroup(preferenceScreen.context).apply {
            buildPreference(this, ProfileHelper.is_out_enabled_on, ProfileHelper.layout)
            visibleKeysOn = arrayOf(ProfileHelper.is_out_enabled.key)
        }

        SimpleSeekBarPreference(preferenceScreen.context).apply {
            buildPreference(this, ProfileHelper.out_x, ProfileHelper.is_out_enabled_on)
            setTitle(R.string.profile_out_x_title)
            setSummary(R.string.profile_out_x_summary)
            setIcon(R.drawable.profile_x)
            min = -99
            max = 199
        }

        SimpleSeekBarPreference(preferenceScreen.context).apply {
            buildPreference(this, ProfileHelper.out_y, ProfileHelper.is_out_enabled_on)
            setTitle(R.string.profile_out_y_title)
            setSummary(R.string.profile_out_y_summary)
            setIcon(R.drawable.profile_y)
            min = -99
            max = 199
        }

        PreferenceCategory(preferenceScreen.context).apply {
            buildPreference(this, ProfileHelper.display)
            setTitle(R.string.profile_display_title)
        }

        SimpleSeekBarPreference(preferenceScreen.context).apply {
            buildPreference(this, ProfileHelper.alpha, ProfileHelper.display)
            setTitle(R.string.profile_alpha_title)
            setIcon(R.drawable.profile_alpha)
            min = 1
            max = 100
        }

        CheckBoxPreference(preferenceScreen.context).apply {
            buildPreference(this, ProfileHelper.is_border_enabled, ProfileHelper.display)
            title = HtmlCompat.fromHtml(getString(R.string.profile_is_border_enabled_title),
                HtmlCompat.FROM_HTML_MODE_COMPACT)
            setSummaryOff(R.string.profile_is_border_enabled_summary_off)
            setSummaryOn(R.string.profile_is_border_enabled_summary_on)
            setIcon(R.drawable.profile_is_out_enabled)
        }

        CheckBoxPreference(preferenceScreen.context).apply {
            buildPreference(this, ProfileHelper.is_keep_screen_on_enabled, ProfileHelper.display)
            setTitle(R.string.profile_is_keep_screen_on_enabled_title)
            setSummaryOff(R.string.profile_is_keep_screen_on_enabled_summary_off)
            setSummaryOn(R.string.profile_is_keep_screen_on_enabled_summary_on)
            setIcon(R.drawable.profile_is_keep_screen_on_enabled)
        }

        SimpleListPreference(preferenceScreen.context).apply {
            buildPreference(this, ProfileHelper.toast, ProfileHelper.display)
            setTitle(R.string.profile_toast_title)
            setIcon(R.drawable.profile_toast)
            entryValues = arrayOf(
                "system_alert_window",
                "system",
                "none"
            )
            setEntries(R.array.profile_toast_entries)
            setDialogTitle(R.string.profile_toast_title)
            setDialogIcon(R.drawable.profile_toast)
        }

        PreferenceCategory(preferenceScreen.context).apply {
            buildPreference(this, ProfileHelper.control)
            setTitle(R.string.profile_control_title)
        }

        CheckBoxPreference(preferenceScreen.context).apply {
            buildPreference(this, ProfileHelper.is_touchable, ProfileHelper.control)
            setTitle(R.string.profile_is_touchable_title)
            setSummaryOff(R.string.profile_is_touchable_summary_off)
            setSummaryOn(R.string.profile_is_touchable_summary_on)
            setIcon(R.drawable.profile_is_touchable)
        }

        SimplePreferenceGroup(preferenceScreen.context).apply {
            buildPreference(this, ProfileHelper.is_touchable_on, ProfileHelper.control)
            visibleKeysOn = arrayOf(ProfileHelper.is_touchable.key)
        }

        Preference(preferenceScreen.context).apply {
            buildPreference(this, ProfileHelper.single_tap, ProfileHelper.is_touchable_on)
            setTitle(R.string.profile_single_tap_title)
            setSummary(R.string.profile_single_tap_summary)
            setIcon(R.drawable.profile_single_tap)
        }

        Preference(preferenceScreen.context).apply {
            buildPreference(this, ProfileHelper.double_tap, ProfileHelper.is_touchable_on)
            setTitle(R.string.profile_double_tap_title)
            setSummary(R.string.profile_double_tap_summary)
            setIcon(R.drawable.profile_double_tap)
        }

        Preference(preferenceScreen.context).apply {
            buildPreference(this, ProfileHelper.long_press, ProfileHelper.is_touchable_on)
            setTitle(R.string.profile_long_press_title)
            setSummary(R.string.profile_long_press_summary)
            setIcon(R.drawable.profile_long_press)
        }

        CheckBoxPreference(preferenceScreen.context).apply {
            buildPreference(this, ProfileHelper.is_move_enabled, ProfileHelper.is_touchable_on)
            setTitle(R.string.profile_is_move_enabled_title)
            setSummaryOff(R.string.profile_is_move_enabled_summary_off)
            setSummaryOn(R.string.profile_is_move_enabled_summary_on)
            setIcon(R.drawable.profile_is_move_enabled)
        }

        CheckBoxPreference(preferenceScreen.context).apply {
            buildPreference(this, ProfileHelper.is_stop_when_screen_off_enabled, ProfileHelper.control)
            setTitle(R.string.profile_is_stop_when_screen_off_enabled_title)
            setSummaryOff(R.string.profile_is_stop_when_screen_off_enabled_summary_off)
            setSummaryOn(R.string.profile_is_stop_when_screen_off_enabled_summary_on)
            setIcon(R.drawable.profile_is_stop_when_screen_off_enabled)
        }

        FooterPreference(preferenceScreen.context).apply {
            preferenceScreen.addPreference(this)
        }
    }
}
