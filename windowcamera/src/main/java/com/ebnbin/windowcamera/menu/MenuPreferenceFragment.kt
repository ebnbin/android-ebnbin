package com.ebnbin.windowcamera.menu

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.ebnbin.eb.activity.openActivity
import com.ebnbin.eb.fragment.openFragment
import com.ebnbin.eb2.about.AboutFragment
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.album.AlbumActivity
import com.jeremyliao.liveeventbus.LiveEventBus

class MenuPreferenceFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.menu_preference_fragment, rootKey)

        findPreference<Preference>(getString(R.string.menu_album))?.apply {
            setOnPreferenceClickListener {
                openActivity<AlbumActivity>()
                LiveEventBus.get("MenuDismissEvent").post(Unit)
                false
            }
        }

        findPreference<Preference>(getString(R.string.menu_about))?.apply {
            setOnPreferenceClickListener {
                openFragment<AboutFragment>()
                LiveEventBus.get("MenuDismissEvent").post(Unit)
                false
            }
        }
    }
}
