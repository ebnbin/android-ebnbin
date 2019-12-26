package com.ebnbin.windowcamera.profile.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.updatePadding
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceGroup
import com.ebnbin.eb.EBApp
import com.ebnbin.eb.dpToPxRound
import com.ebnbin.eb.getSharedPreferencesName
import com.ebnbin.windowcamera.profile.ProfileHelper
import com.ebnbin.windowcamera.profile.ProfileSp

abstract class BaseProfileFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName =
            EBApp.instance.getSharedPreferencesName(ProfileHelper.getSharedPreferencesNamePostfix())

        preferenceScreen = preferenceManager.createPreferenceScreen(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView.updatePadding(bottom = requireContext().dpToPxRound(100f))
        listView.clipToPadding = false
        listView.scrollBarStyle = View.SCROLLBARS_OUTSIDE_OVERLAY
    }

    protected fun <T> buildPreference(preference: Preference, sp: ProfileSp<T>, groupSp: ProfileSp<Unit>? = null) {
        preference.key = sp.key
        val builder = sp.builder()
        if (builder.defaultValue != Unit) {
            preference.setDefaultValue(builder.defaultValue)
        }
        val preferenceGroup = if (groupSp == null) {
            preferenceScreen
        } else {
            findPreference<PreferenceGroup>(groupSp.key) ?: throw RuntimeException()
        }
        preferenceGroup.addPreference(preference)
        builder.isVisible?.let { preference.isVisible = it }
        builder.isEnabled?.let { preference.isEnabled = it }
    }
}
