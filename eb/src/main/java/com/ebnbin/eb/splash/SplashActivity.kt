package com.ebnbin.eb.splash

import android.os.Bundle
import com.ebnbin.eb.activity.EBActivity
import com.ebnbin.eb.util.ebApp

class SplashActivity : EBActivity() {
    override fun onInitArguments(savedInstanceState: Bundle?, extras: Bundle) {
        extras.putSerializable(KEY_FRAGMENT_CLASS, ebApp.splashFragment)
        super.onInitArguments(savedInstanceState, extras)
    }
}
