package com.ebnbin.windowcamera.splash

import android.os.Bundle
import com.ebnbin.eb2.activity.EBActivity

class SplashActivity : EBActivity() {
    override fun onInitArguments(savedInstanceState: Bundle?, extras: Bundle) {
        extras.putSerializable(KEY_FRAGMENT_CLASS, SplashFragment::class.java)
        super.onInitArguments(savedInstanceState, extras)
    }
}
