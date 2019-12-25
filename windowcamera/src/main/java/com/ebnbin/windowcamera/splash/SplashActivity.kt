package com.ebnbin.windowcamera.splash

import androidx.fragment.app.Fragment
import com.ebnbin.eb.FragmentActivity

class SplashActivity : FragmentActivity() {
    override val fragmentClass: Class<out Fragment>
        get() = SplashFragment::class.java
}
