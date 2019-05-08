package com.ebnbin.windowcamera

import com.ebnbin.eb.EBApplication
import com.ebnbin.eb.debug.BaseDebugPageFragment
import com.ebnbin.eb.splash.EBSplashFragment
import com.ebnbin.windowcamera.debug.DebugPageFragment
import com.ebnbin.windowcamera.splash.SplashFragment

class AppApplication : EBApplication() {
    override val debugPageFragmentClass: Class<out BaseDebugPageFragment>? = DebugPageFragment::class.java

    override val splashFragment: Class<out EBSplashFragment> = SplashFragment::class.java
}
