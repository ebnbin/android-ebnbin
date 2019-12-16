package com.ebnbin.windowcamera.splash

import androidx.fragment.app.Fragment
import com.ebnbin.eb2.activity.EBFragmentActivity

class SplashActivity : EBFragmentActivity() {
    override val fragmentClass: Class<out Fragment>
        get() = SplashFragment::class.java
}
