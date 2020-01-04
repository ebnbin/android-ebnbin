package com.ebnbin.sample.dev

import android.os.Bundle
import android.view.View
import androidx.preference.PreferenceFragmentCompat
import com.ebnbin.eb.activity.openActivity
import com.ebnbin.eb.preference.Preference
import com.ebnbin.eb.preference.PreferenceCategory
import com.ebnbin.sample.touch.TouchSampleActivity

internal class SampleDevPageFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceScreen = preferenceManager.createPreferenceScreen(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //*************************************************************************************************************

        val samplePreferenceGroup = PreferenceCategory(requireContext()).also {
            preferenceScreen.addPreference(it)
            it.title = "Sample"
        }

        Preference(requireContext()).also {
            samplePreferenceGroup.addPreference(it)
            it.title = "TouchSampleActivity"
            it.setOnPreferenceClickListener {
                openActivity<TouchSampleActivity>()
                true
            }
        }
    }
}
