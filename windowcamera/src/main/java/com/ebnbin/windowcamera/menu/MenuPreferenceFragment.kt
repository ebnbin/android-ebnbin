package com.ebnbin.windowcamera.menu

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.SwitchPreference
import com.ebnbin.eb.about.AboutFragment
import com.ebnbin.eb.library.Libraries
import com.ebnbin.eb.preference.EBPreferenceFragment
import com.ebnbin.eb.util.AppHelper
import com.ebnbin.eb.util.IntentHelper
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.album.AlbumActivity
import com.ebnbin.windowcamera.util.SpManager

class MenuPreferenceFragment : EBPreferenceFragment() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.menu_preference_fragment, rootKey)

        findPreference<Preference>(getString(R.string.menu_album))?.apply {
            setOnPreferenceClickListener {
                IntentHelper.startActivityFromActivity(requireActivity(), AlbumActivity::class.java)
                Libraries.eventBus.post(MenuDismissEvent)
                false
            }
        }

        findPreference<SwitchPreference>(SpManager.is_night_mode_enabled.requireKey())?.apply {
            setOnPreferenceChangeListener { _, newValue ->
                newValue as Boolean
                AppHelper.setNightMode(
                    if (newValue) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
                true
            }
            setOnPreferenceClickListener {
                Libraries.eventBus.post(MenuDismissEvent)
                false
            }
        }

        findPreference<Preference>(getString(R.string.menu_about))?.apply {
            setOnPreferenceClickListener {
                IntentHelper.startFragmentFromActivity(requireActivity(), AboutFragment.intent())
                Libraries.eventBus.post(MenuDismissEvent)
                false
            }
        }
    }
}
