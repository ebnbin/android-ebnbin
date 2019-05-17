package com.ebnbin.windowcamera.main

import android.os.Bundle
import com.ebnbin.eb.activity.EBActivity
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.profile.ProfileHelper

class MainActivity : EBActivity() {
    override fun onInitArguments(savedInstanceState: Bundle?, extras: Bundle) {
        val themeStyleId = when (ProfileHelper.profile.value) {
            "default" -> R.style.ProfileTheme_LightBlue
            "custom_1" -> R.style.ProfileTheme_Red
            "custom_2" -> R.style.ProfileTheme_Green
            else -> throw RuntimeException()
        }
        extras.putInt(KEY_THEME_STYLE_ID, themeStyleId)
        extras.putSerializable(KEY_FRAGMENT_CLASS, MainFragment::class.java)
        super.onInitArguments(savedInstanceState, extras)
    }
}
