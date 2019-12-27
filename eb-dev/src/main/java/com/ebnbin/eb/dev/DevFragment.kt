package com.ebnbin.eb.dev

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.preference.PreferenceFragmentCompat
import com.ebnbin.eb.preference.Preference
import com.ebnbin.eb.requireArgument

internal class DevFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = DevSpManager.name
        preferenceScreen = preferenceManager.createPreferenceScreen(requireContext())

        Preference(requireContext()).also {
            preferenceScreen.addPreference(it)
            it.title = "Calling Activity"
            it.summary = requireArgument(KEY_CALLING_ACTIVITY)
            it.isIconSpaceReserved = false
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
