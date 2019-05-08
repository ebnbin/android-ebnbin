package com.ebnbin.eb.splash

import android.os.Bundle
import com.ebnbin.eb.activity.EBActivity
import com.ebnbin.eb.util.ebApp

class SplashActivity : EBActivity() {
    override fun onInitArguments(savedInstanceState: Bundle?, extras: Bundle) {
        super.onInitArguments(savedInstanceState, extras)
        fragmentClass = ebApp.splashFragment
    }
}
