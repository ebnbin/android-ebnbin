package com.ebnbin.windowcamera.splash

import com.ebnbin.eb.app.EBActivity
import com.ebnbin.eb.splash.EBSplashActivity
import com.ebnbin.windowcamera.main.MainActivity

/**
 * 闪屏页面.
 */
class SplashActivity : EBSplashActivity() {
    override val mainActivityClass: Class<out EBActivity> = MainActivity::class.java
}
