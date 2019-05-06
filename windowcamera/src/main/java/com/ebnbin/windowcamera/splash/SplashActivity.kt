package com.ebnbin.windowcamera.splash

import com.ebnbin.eb.app.EBActivity
import com.ebnbin.eb.dev.DevHelper
import com.ebnbin.eb.splash.EBSplashActivity
import com.ebnbin.eb.util.AppHelper
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.camera.CameraHelper
import com.ebnbin.windowcamera.dev.Device
import com.ebnbin.windowcamera.main.MainActivity

/**
 * 闪屏页面.
 */
class SplashActivity : EBSplashActivity() {
    override val mainActivityClass: Class<out EBActivity> = MainActivity::class.java

    override fun init(): Boolean {
        if (!super.init()) return false
        try {
            if (!CameraHelper.isValid()) throw RuntimeException()
            DevHelper.device(Device())
        } catch (throwable: Throwable) {
            AppHelper.toast(this, R.string.camera_error)
            return false
        }
        return true
    }
}
