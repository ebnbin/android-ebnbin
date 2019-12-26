package com.ebnbin.windowcamera.profile.fragment

import android.os.Bundle
import androidx.core.text.HtmlCompat
import com.ebnbin.eb.getOrNull
import com.ebnbin.eb.preference.CheckBoxPreference
import com.ebnbin.eb.preference.ListPreference
import com.ebnbin.eb.preference.PreferenceCategory
import com.ebnbin.eb.preference.PreferenceGroup
import com.ebnbin.eb.preference.SeekBarPreference
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.profile.ProfileHelper
import com.ebnbin.windowcamera.profile.enumeration.ProfileGesture
import com.ebnbin.windowcamera.profile.enumeration.ProfileRatio
import com.ebnbin.windowcamera.profile.enumeration.ProfileToast

class WindowProfileFragment : BaseProfileFragment() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)
        PreferenceCategory(requireContext()).apply {
            buildPreference(this, ProfileHelper.layout)
            setTitle(R.string.profile_layout_title)
        }

        SeekBarPreference(requireContext()).apply {
            buildPreference(this, ProfileHelper.size, ProfileHelper.layout)
            setTitle(R.string.profile_size_title)
            setSummary(R.string.profile_size_summary)
            setIcon(R.drawable.profile_size)
            min = 1
            max = 100
        }

        ListPreference(requireContext()).apply {
            buildPreference(this, ProfileHelper.ratio, ProfileHelper.layout)
            setTitle(R.string.profile_ratio_title)
            setIcon(R.drawable.profile_ratio)
            entryValues = ProfileRatio.entryValues()
            entries = ProfileRatio.entries()
            setDialogTitle(R.string.profile_ratio_title)
            setDialogIcon(R.drawable.profile_ratio)
        }

        CheckBoxPreference(requireContext()).apply {
            buildPreference(this, ProfileHelper.is_out_enabled, ProfileHelper.layout)
            setTitle(R.string.profile_is_out_enabled_title)
            setSummaryOff(R.string.profile_is_out_enabled_summary_off)
            setSummaryOn(R.string.profile_is_out_enabled_summary_on)
            setIcon(R.drawable.profile_is_out_enabled)
        }

        PreferenceGroup(requireContext()).apply {
            buildPreference(this, ProfileHelper.is_out_enabled_off, ProfileHelper.layout)
            setVisibleProvider(ProfileHelper.is_out_enabled.key) {
                val sharedPreferences = it.sharedPreferences ?: return@setVisibleProvider null
                val result = !(sharedPreferences.getOrNull<Boolean>(ProfileHelper.is_out_enabled.key)
                    ?: return@setVisibleProvider null)
                result
            }
        }

        SeekBarPreference(requireContext()).apply {
            buildPreference(this, ProfileHelper.in_x, ProfileHelper.is_out_enabled_off)
            setTitle(R.string.profile_in_x_title)
            setSummary(R.string.profile_in_x_summary)
            setIcon(R.drawable.profile_x)
            min = 0
            max = 100
        }

        SeekBarPreference(requireContext()).apply {
            buildPreference(this, ProfileHelper.in_y, ProfileHelper.is_out_enabled_off)
            setTitle(R.string.profile_in_y_title)
            setSummary(R.string.profile_in_y_summary)
            setIcon(R.drawable.profile_y)
            min = 0
            max = 100
        }

        PreferenceGroup(requireContext()).apply {
            buildPreference(this, ProfileHelper.is_out_enabled_on, ProfileHelper.layout)
            setVisibleProvider(ProfileHelper.is_out_enabled.key) {
                val sharedPreferences = it.sharedPreferences ?: return@setVisibleProvider null
                val result = sharedPreferences.getOrNull<Boolean>(ProfileHelper.is_out_enabled.key)
                    ?: return@setVisibleProvider null
                result
            }
        }

        SeekBarPreference(requireContext()).apply {
            buildPreference(this, ProfileHelper.out_x, ProfileHelper.is_out_enabled_on)
            setTitle(R.string.profile_out_x_title)
            setSummary(R.string.profile_out_x_summary)
            setIcon(R.drawable.profile_x)
            min = -99
            max = 199
        }

        SeekBarPreference(requireContext()).apply {
            buildPreference(this, ProfileHelper.out_y, ProfileHelper.is_out_enabled_on)
            setTitle(R.string.profile_out_y_title)
            setSummary(R.string.profile_out_y_summary)
            setIcon(R.drawable.profile_y)
            min = -99
            max = 199
        }

        PreferenceCategory(requireContext()).apply {
            buildPreference(this, ProfileHelper.display)
            setTitle(R.string.profile_display_title)
        }

        SeekBarPreference(requireContext()).apply {
            buildPreference(this, ProfileHelper.alpha, ProfileHelper.display)
            setTitle(R.string.profile_alpha_title)
            setIcon(R.drawable.profile_alpha)
            min = 1
            max = 100
        }

        SeekBarPreference(requireContext()).apply {
            buildPreference(this, ProfileHelper.radius, ProfileHelper.display)
            setTitle(R.string.profile_radius_title)
            setSummary(R.string.profile_radius_summary)
            setIcon(R.drawable.profile_radius)
            min = 0
            max = 100
        }

        CheckBoxPreference(requireContext()).apply {
            buildPreference(this, ProfileHelper.is_border_enabled, ProfileHelper.display)
            title = HtmlCompat.fromHtml(getString(R.string.profile_is_border_enabled_title),
                HtmlCompat.FROM_HTML_MODE_COMPACT)
            setSummaryOff(R.string.profile_is_border_enabled_summary_off)
            setSummaryOn(R.string.profile_is_border_enabled_summary_on)
            setIcon(R.drawable.profile_is_border_enabled)
        }

        PreferenceGroup(requireContext()).apply {
            buildPreference(this, ProfileHelper.is_border_enabled_on, ProfileHelper.display)
            setVisibleProvider(ProfileHelper.is_border_enabled.key) {
                val sharedPreferences = it.sharedPreferences ?: return@setVisibleProvider null
                val result = sharedPreferences.getOrNull<Boolean>(ProfileHelper.is_border_enabled.key)
                    ?: return@setVisibleProvider null
                result
            }
        }

        SeekBarPreference(requireContext()).apply {
            buildPreference(this, ProfileHelper.border_width, ProfileHelper.is_border_enabled_on)
            setTitle(R.string.profile_border_width_title)
            setSummary(R.string.profile_border_width_summary)
            setIcon(R.drawable.profile_border_width)
            min = 1
            max = 32
        }

        CheckBoxPreference(requireContext()).apply {
            buildPreference(this, ProfileHelper.is_keep_screen_on_enabled, ProfileHelper.display)
            setTitle(R.string.profile_is_keep_screen_on_enabled_title)
            setSummaryOff(R.string.profile_is_keep_screen_on_enabled_summary_off)
            setSummaryOn(R.string.profile_is_keep_screen_on_enabled_summary_on)
            setIcon(R.drawable.profile_is_keep_screen_on_enabled)
        }

        ListPreference(requireContext()).apply {
            buildPreference(this, ProfileHelper.toast, ProfileHelper.display)
            setTitle(R.string.profile_toast_title)
            setIcon(R.drawable.profile_toast)
            entryValues = ProfileToast.entryValues()
            entries = ProfileToast.entries()
            setDialogTitle(R.string.profile_toast_title)
            setDialogIcon(R.drawable.profile_toast)
        }

        PreferenceCategory(requireContext()).apply {
            buildPreference(this, ProfileHelper.control)
            setTitle(R.string.profile_control_title)
        }

        CheckBoxPreference(requireContext()).apply {
            buildPreference(this, ProfileHelper.is_touchable, ProfileHelper.control)
            setTitle(R.string.profile_is_touchable_title)
            setSummaryOff(R.string.profile_is_touchable_summary_off)
            setSummaryOn(R.string.profile_is_touchable_summary_on)
            setIcon(R.drawable.profile_is_touchable)
        }

        PreferenceGroup(requireContext()).apply {
            buildPreference(this, ProfileHelper.is_touchable_on, ProfileHelper.control)
            setVisibleProvider(ProfileHelper.is_touchable.key) {
                val sharedPreferences = it.sharedPreferences ?: return@setVisibleProvider null
                val result = sharedPreferences.getOrNull<Boolean>(ProfileHelper.is_touchable.key)
                    ?: return@setVisibleProvider null
                result
            }
        }

        ListPreference(requireContext()).apply {
            buildPreference(this, ProfileHelper.single_tap, ProfileHelper.is_touchable_on)
            setTitle(R.string.profile_single_tap_title)
            setIcon(R.drawable.profile_single_tap)
            entryValues = ProfileGesture.entryValues()
            entries = ProfileGesture.entries()
            setDialogTitle(R.string.profile_single_tap_title)
            setDialogIcon(R.drawable.profile_single_tap)
        }

        ListPreference(requireContext()).apply {
            buildPreference(this, ProfileHelper.double_tap, ProfileHelper.is_touchable_on)
            setTitle(R.string.profile_double_tap_title)
            setIcon(R.drawable.profile_double_tap)
            entryValues = ProfileGesture.entryValues()
            entries = ProfileGesture.entries()
            setDialogTitle(R.string.profile_double_tap_title)
            setDialogIcon(R.drawable.profile_double_tap)
        }

        ListPreference(requireContext()).apply {
            buildPreference(this, ProfileHelper.long_press, ProfileHelper.is_touchable_on)
            setTitle(R.string.profile_long_press_title)
            setIcon(R.drawable.profile_long_press)
            entryValues = ProfileGesture.entryValues()
            entries = ProfileGesture.entries()
            setDialogTitle(R.string.profile_long_press_title)
            setDialogIcon(R.drawable.profile_long_press)
        }

        CheckBoxPreference(requireContext()).apply {
            buildPreference(this, ProfileHelper.is_move_enabled, ProfileHelper.is_touchable_on)
            setTitle(R.string.profile_is_move_enabled_title)
            setSummaryOff(R.string.profile_is_move_enabled_summary_off)
            setSummaryOn(R.string.profile_is_move_enabled_summary_on)
            setIcon(R.drawable.profile_is_move_enabled)
        }

        CheckBoxPreference(requireContext()).apply {
            buildPreference(this, ProfileHelper.is_stop_when_screen_off_enabled, ProfileHelper.control)
            setTitle(R.string.profile_is_stop_when_screen_off_enabled_title)
            setSummaryOff(R.string.profile_is_stop_when_screen_off_enabled_summary_off)
            setSummaryOn(R.string.profile_is_stop_when_screen_off_enabled_summary_on)
            setIcon(R.drawable.profile_is_stop_when_screen_off_enabled)
        }
    }
}
