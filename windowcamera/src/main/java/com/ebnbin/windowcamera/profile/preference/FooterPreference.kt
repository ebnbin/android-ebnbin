package com.ebnbin.windowcamera.profile.preference

import android.content.Context
import androidx.preference.Preference
import com.ebnbin.windowcamera.R

class FooterPreference(context: Context) : Preference(context) {
    init {
        layoutResource = R.layout.profile_footer_preference
    }
}
