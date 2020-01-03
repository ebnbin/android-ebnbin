package com.ebnbin.ebapp.dev

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import com.ebnbin.eb.getNightModeToString
import com.ebnbin.eb.preference.DropDownPreference
import com.ebnbin.eb.preference.PreferenceCategory
import com.ebnbin.ebapp.EBAppSpManager

internal class EBAppDevPageFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = EBAppSpManager.name
        preferenceScreen = preferenceManager.createPreferenceScreen(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //*************************************************************************************************************

        val nightModePreferenceGroup = PreferenceCategory(requireContext()).also {
            preferenceScreen.addPreference(it)
            it.title = "Night Mode"
        }

        DropDownPreference(requireContext()).also {
            it.key = EBAppSpManager.night_mode.key
            it.setDefaultValue(EBAppSpManager.night_mode.defaultValue)
            nightModePreferenceGroup.addPreference(it)
            it.title = "Night Mode"
            val nightModes = arrayOf(
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
                AppCompatDelegate.MODE_NIGHT_NO,
                AppCompatDelegate.MODE_NIGHT_YES,
                AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY,
                AppCompatDelegate.MODE_NIGHT_UNSPECIFIED
            )
            it.entryValues = nightModes
                .map { nightMode -> nightMode.toString() }
                .toTypedArray()
            it.entries = nightModes
                .map { nightMode -> getNightModeToString(nightMode) }
                .toTypedArray()
            it.dialogTitle = it.title
        }
    }
}
