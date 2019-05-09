package com.ebnbin.windowcamera.splash

import android.os.Bundle
import com.ebnbin.eb.dev.DevHelper
import com.ebnbin.eb.splash.EBSplashFragment
import com.ebnbin.eb.util.AppHelper
import com.ebnbin.eb.util.IntentHelper
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.camera.CameraHelper
import com.ebnbin.windowcamera.dev.Report
import com.ebnbin.windowcamera.main.MainActivity

class SplashFragment : EBSplashFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            if (!CameraHelper.isValid()) throw RuntimeException()
        } catch (throwable: Throwable) {
            DevHelper.report(throwable)
            AppHelper.toast(requireContext(), R.string.camera_error_splash)
            finish()
            return
        }
        DevHelper.report(Report())
        IntentHelper.startActivityFromFragment(this, MainActivity::class.java)
        finish()
    }

    override val isBackFinishEnabled: Boolean = false
}
