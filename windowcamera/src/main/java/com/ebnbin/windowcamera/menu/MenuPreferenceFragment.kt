package com.ebnbin.windowcamera.menu

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.SwitchPreference
import com.ebnbin.eb.about.AboutFragment
import com.ebnbin.eb.library.Libraries
import com.ebnbin.eb.permission.PermissionFragment
import com.ebnbin.eb.preference.EBPreferenceFragment
import com.ebnbin.eb.util.AppHelper
import com.ebnbin.eb.util.IntentHelper
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.album.AlbumFragment

class MenuPreferenceFragment : EBPreferenceFragment(), PermissionFragment.Callback {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.menu_preference_fragment, rootKey)

        findPreference<Preference>(getString(R.string.menu_album))?.apply {
            setOnPreferenceClickListener {
                PermissionFragment.start(childFragmentManager,
                    arrayListOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                false
            }
        }

        findPreference<SwitchPreference>(getString(R.string.menu_is_night_mode))?.apply {
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
                Libraries.eventBus.post(MenuDismissEvent)
                IntentHelper.startFragmentFromFragment(this@MenuPreferenceFragment, AboutFragment.intent())
                false
            }
        }
    }

    override fun onPermissionsResult(permissions: ArrayList<String>, granted: Boolean, extraData: Bundle) {
        if (granted) {
            Libraries.eventBus.post(MenuDismissEvent)
            IntentHelper.startFragmentFromFragment(this@MenuPreferenceFragment, AlbumFragment::class.java)
        }
    }
}
