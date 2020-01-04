package com.ebnbin.windowcamera.dev

import android.os.Bundle
import android.view.View
import androidx.preference.PreferenceFragmentCompat
import com.ebnbin.eb.activity.openActivity
import com.ebnbin.eb.fragment.openFragment
import com.ebnbin.eb.preference.Preference
import com.ebnbin.eb.preference.PreferenceCategory
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.album2.AlbumActivity
import com.ebnbin.windowcamera.viewer.ViewerActivity
import com.ebnbin.windowcamera.viewer.ViewerFragment

class AppDevPageFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceScreen = preferenceManager.createPreferenceScreen(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //*************************************************************************************************************

        val appPreferenceGroup = PreferenceCategory(requireContext()).also {
            preferenceScreen.addPreference(it)
            it.title = "App"
        }

        Preference(requireContext()).also {
            appPreferenceGroup.addPreference(it)
            it.title = "AlbumActivity"
            it.setOnPreferenceClickListener {
                openActivity<AlbumActivity>()
                true
            }
        }

        Preference(requireContext()).also {
            appPreferenceGroup.addPreference(it)
            it.title = "ViewerActivity"
            it.setOnPreferenceClickListener {
                openFragment<ViewerFragment>(
                    theme = R.style.EBAppTheme_Viewer,
                    fragmentActivityClass = ViewerActivity::class.java
                )
                true
            }
        }
    }
}
