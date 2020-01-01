package com.ebnbin.eb.dev

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.preference.PreferenceFragmentCompat
import com.ebnbin.eb.fragment.requireArgument
import com.ebnbin.eb.preference.Preference
import com.ebnbin.eb.preference.SeekBarPreference

internal class EBDevPageFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = DevSpManager.name
        preferenceScreen = preferenceManager.createPreferenceScreen(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Preference(requireContext()).also {
            preferenceScreen.addPreference(it)
            it.title = "Calling Activity"
            it.summary = requireArgument(KEY_CALLING_ACTIVITY)
        }

        Preference(requireContext()).also {
            fun summary(
                floatingX: Int = DevSpManager.dev_floating_x.value,
                floatingY: Int = DevSpManager.dev_floating_y.value
            ): CharSequence {
                return "$floatingX,$floatingY"
            }

            preferenceScreen.addPreference(it)
            it.title = "悬浮按钮位置（像素）"
            it.summary = summary()
            DevSpManager.dev_floating_x.addOnChange(viewLifecycleOwner.lifecycle) { _, newValue ->
                it.summary = summary(floatingX = newValue)
                false
            }
            DevSpManager.dev_floating_y.addOnChange(viewLifecycleOwner.lifecycle) { _, newValue ->
                it.summary = summary(floatingY = newValue)
                false
            }
        }

        SeekBarPreference(requireContext()).also {
            it.key = DevSpManager.dev_floating_hide_duration.key
            it.setDefaultValue(DevSpManager.dev_floating_hide_duration.defaultValue)
            preferenceScreen.addPreference(it)
            it.title = "双击悬浮按钮临时隐藏时长（秒）"
            it.min = 1
            it.max = 60
            it.seekBarIncrement = 1
        }
    }

    companion object {
        private const val KEY_CALLING_ACTIVITY: String = "calling_activity"

        fun createArguments(
            callingActivity: CharSequence
        ): Bundle {
            return bundleOf(
                KEY_CALLING_ACTIVITY to callingActivity
            )
        }
    }
}