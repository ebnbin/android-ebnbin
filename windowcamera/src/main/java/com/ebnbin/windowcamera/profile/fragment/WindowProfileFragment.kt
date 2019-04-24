package com.ebnbin.windowcamera.profile.fragment

import android.os.Bundle
import androidx.preference.CheckBoxPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.SeekBarPreference
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.preference.FooterPreference
import com.ebnbin.windowcamera.preference.SimplePreferenceGroup
import com.ebnbin.windowcamera.preference.SimpleSeekBarPreference
import com.ebnbin.windowcamera.profile.ProfileSpManager

class WindowProfileFragment : BaseProfileFragment() {
    private lateinit var layoutPreference: PreferenceCategory
    private lateinit var sizePreference: SeekBarPreference
    private lateinit var ratioPreference: ListPreference
    private lateinit var isOutEnabledPreference: CheckBoxPreference
    private lateinit var isOutEnabledOffPreferenceGroup: SimplePreferenceGroup
    private lateinit var inXPreference: SimpleSeekBarPreference
    private lateinit var inYPreference: SimpleSeekBarPreference
    private lateinit var isOutEnabledOnPreferenceGroup: SimplePreferenceGroup
    private lateinit var outXPreference: SimpleSeekBarPreference
    private lateinit var outYPreference: SimpleSeekBarPreference
    private lateinit var displayPreference: PreferenceCategory
    private lateinit var alphaPreference: SeekBarPreference
    private lateinit var isKeepScreenOnEnabledPreference: CheckBoxPreference
    private lateinit var controlPreference: PreferenceCategory
    private lateinit var isTouchablePreference: CheckBoxPreference
    private lateinit var isTouchableOnPreferenceGroup: SimplePreferenceGroup
    private lateinit var singleTapPreference: Preference
    private lateinit var doubleTapPreference: Preference
    private lateinit var longPressPreference: Preference
    private lateinit var isMoveEnabled: CheckBoxPreference
    private lateinit var footerPreference: FooterPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)
        layoutPreference = PreferenceCategory(requireContext()).apply {
            key = ProfileSpManager.layout.key
            setTitle(R.string.profile_layout)
            preferenceScreen.addPreference(this)
        }

        sizePreference = SeekBarPreference(requireContext()).apply {
            key = ProfileSpManager.size.key
            setDefaultValue(ProfileSpManager.size.getDefaultValue())
            setTitle(R.string.profile_size)
            setSummary(R.string.profile_size_summary)
            min = 1
            max = 100
            preferenceScreen.addPreference(this)
        }

        ratioPreference = ListPreference(requireContext()).apply {
            key = ProfileSpManager.ratio.key
            setDefaultValue(ProfileSpManager.ratio.getDefaultValue())
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
            preferenceScreen.addPreference(this)
        }

        isOutEnabledPreference = CheckBoxPreference(requireContext()).apply {
            key = ProfileSpManager.is_out_enabled.key
            setDefaultValue(ProfileSpManager.is_out_enabled.getDefaultValue())
            setTitle(R.string.profile_is_out_enabled)
            setSummaryOff(R.string.profile_is_out_enabled_summary_off)
            setSummaryOn(R.string.profile_is_out_enabled_summary_on)
            preferenceScreen.addPreference(this)
        }

        isOutEnabledOffPreferenceGroup = SimplePreferenceGroup(requireContext()).apply {
            key = ProfileSpManager.is_out_enabled_off.key
            visibleTwoStatePreferences = Pair(setOf(isOutEnabledPreference), null)
            preferenceScreen.addPreference(this)
        }

        inXPreference = SimpleSeekBarPreference(requireContext()).apply {
            key = ProfileSpManager.in_x.key
            setDefaultValue(ProfileSpManager.in_x.getDefaultValue())
            setTitle(R.string.profile_in_x)
            setSummary(R.string.profile_in_x_summary)
            min = 0
            max = 100
            isOutEnabledOffPreferenceGroup.addPreference(this)
        }

        inYPreference = SimpleSeekBarPreference(requireContext()).apply {
            key = ProfileSpManager.in_y.key
            setDefaultValue(ProfileSpManager.in_y.getDefaultValue())
            setTitle(R.string.profile_in_y)
            setSummary(R.string.profile_in_y_summary)
            min = 0
            max = 100
            isOutEnabledOffPreferenceGroup.addPreference(this)
        }

        isOutEnabledOnPreferenceGroup = SimplePreferenceGroup(requireContext()).apply {
            key = ProfileSpManager.is_out_enabled_on.key
            visibleTwoStatePreferences = Pair(null, setOf(isOutEnabledPreference))
            preferenceScreen.addPreference(this)
        }

        outXPreference = SimpleSeekBarPreference(requireContext()).apply {
            key = ProfileSpManager.out_x.key
            setDefaultValue(ProfileSpManager.out_x.getDefaultValue())
            setTitle(R.string.profile_out_x)
            setSummary(R.string.profile_out_x_summary)
            min = -99
            max = 199
            isOutEnabledOnPreferenceGroup.addPreference(this)
        }

        outYPreference = SimpleSeekBarPreference(requireContext()).apply {
            key = ProfileSpManager.out_y.key
            setDefaultValue(ProfileSpManager.out_y.getDefaultValue())
            setTitle(R.string.profile_out_y)
            setSummary(R.string.profile_out_y_summary)
            min = -99
            max = 199
            isOutEnabledOnPreferenceGroup.addPreference(this)
        }

        displayPreference = PreferenceCategory(requireContext()).apply {
            key = ProfileSpManager.display.key
            setTitle(R.string.profile_display)
            preferenceScreen.addPreference(this)
        }

        alphaPreference = SeekBarPreference(requireContext()).apply {
            key = ProfileSpManager.alpha.key
            setDefaultValue(ProfileSpManager.alpha.getDefaultValue())
            setTitle(R.string.profile_alpha)
            min = 1
            max = 100
            preferenceScreen.addPreference(this)
        }

        isKeepScreenOnEnabledPreference = CheckBoxPreference(requireContext()).apply {
            key = ProfileSpManager.is_keep_screen_on_enabled.key
            setDefaultValue(ProfileSpManager.is_keep_screen_on_enabled.getDefaultValue())
            setTitle(R.string.profile_is_keep_screen_on_enabled)
            setSummaryOff(R.string.profile_is_keep_screen_on_enabled_summary_off)
            setSummaryOn(R.string.profile_is_keep_screen_on_enabled_summary_on)
            preferenceScreen.addPreference(this)
        }

        controlPreference = PreferenceCategory(requireContext()).apply {
            key = ProfileSpManager.control.key
            setTitle(R.string.profile_control)
            preferenceScreen.addPreference(this)
        }

        isTouchablePreference = CheckBoxPreference(requireContext()).apply {
            key = ProfileSpManager.is_touchable.key
            setDefaultValue(ProfileSpManager.is_touchable.getDefaultValue())
            setTitle(R.string.profile_is_touchable)
            setSummaryOff(R.string.profile_is_touchable_summary_off)
            setSummaryOn(R.string.profile_is_touchable_summary_on)
            preferenceScreen.addPreference(this)
        }

        isTouchableOnPreferenceGroup = SimplePreferenceGroup(requireContext()).apply {
            key = ProfileSpManager.is_touchable_on.key
            visibleTwoStatePreferences = Pair(null, setOf(isTouchablePreference))
            preferenceScreen.addPreference(this)
        }

        singleTapPreference = Preference(requireContext()).apply {
            key = ProfileSpManager.single_tap.key
            setTitle(R.string.profile_single_tap)
            setSummary(R.string.profile_single_tap_summary)
            isTouchableOnPreferenceGroup.addPreference(this)
        }

        doubleTapPreference = Preference(requireContext()).apply {
            key = ProfileSpManager.double_tap.key
            setTitle(R.string.profile_double_tap)
            setSummary(R.string.profile_double_tap_summary)
            isTouchableOnPreferenceGroup.addPreference(this)
        }

        longPressPreference = Preference(requireContext()).apply {
            key = ProfileSpManager.long_press.key
            setTitle(R.string.profile_long_press)
            setSummary(R.string.profile_long_press_summary)
            isTouchableOnPreferenceGroup.addPreference(this)
        }

        isMoveEnabled = CheckBoxPreference(requireContext()).apply {
            key = ProfileSpManager.is_move_enabled.key
            setDefaultValue(ProfileSpManager.is_move_enabled.getDefaultValue())
            setTitle(R.string.profile_is_move_enabled)
            setSummaryOff(R.string.profile_is_move_enabled_summary_off)
            setSummaryOn(R.string.profile_is_move_enabled_summary_on)
            isTouchableOnPreferenceGroup.addPreference(this)
        }

        footerPreference = FooterPreference(requireContext()).apply {
            preferenceScreen.addPreference(this)
        }
    }
}
