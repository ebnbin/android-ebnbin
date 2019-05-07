package com.ebnbin.windowcamera.splash

import android.os.Bundle
import com.ebnbin.eb.app.EBActivity
import com.ebnbin.eb.dev.DevHelper
import com.ebnbin.eb.splash.EBSplashFragment
import com.ebnbin.eb.util.AppHelper
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.camera.CameraHelper
import com.ebnbin.windowcamera.dev.Device
import com.ebnbin.windowcamera.main.MainFragment

class SplashFragment : EBSplashFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            if (!CameraHelper.isValid()) throw RuntimeException()
            DevHelper.device(Device())
            EBActivity.startFragmentFromFragment(this, MainFragment::class.java, MainFragment.createIntent())
        } catch (throwable: Throwable) {
            AppHelper.toast(requireContext(), R.string.camera_error)
        }
        activity?.finish()
    }

    override val isBackFinishEnabled: Boolean = false
}
