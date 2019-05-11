package com.ebnbin.windowcamera.splash

import android.Manifest
import android.os.Bundle
import androidx.core.content.edit
import com.ebnbin.eb.dev.DevHelper
import com.ebnbin.eb.dialog.Cancel
import com.ebnbin.eb.dialog.SimpleDialogFragment
import com.ebnbin.eb.permission.PermissionFragment
import com.ebnbin.eb.permission.PermissionHelper
import com.ebnbin.eb.sharedpreferences.SharedPreferencesHelper
import com.ebnbin.eb.sharedpreferences.get
import com.ebnbin.eb.splash.EBSplashFragment
import com.ebnbin.eb.util.AppHelper
import com.ebnbin.eb.util.IntentHelper
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.camera.CameraHelper
import com.ebnbin.windowcamera.dev.Report
import com.ebnbin.windowcamera.main.MainActivity

class SplashFragment : EBSplashFragment(), SimpleDialogFragment.Callback, PermissionFragment.Callback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            if (PermissionHelper.isPermissionsGranted(permissions)) {
                onPermissionsGranted()
            } else {
                SimpleDialogFragment.start(childFragmentManager, SimpleDialogFragment.Builder(
                    message = getString(R.string.splash_message),
                    positive = getString(R.string.splash_positive),
                    negative = getString(R.string.splash_negative),
                    cancel = Cancel.NOT_CANCELABLE
                ), "splash")
            }
        }
    }
    override fun onDialogPositive(extraData: Bundle): Boolean {
        PermissionFragment.start(childFragmentManager, arrayListOf(Manifest.permission.CAMERA))
        return true
    }

    override fun onDialogNegative(extraData: Bundle): Boolean {
        finish()
        return true
    }

    override fun onDialogNeutral(extraData: Bundle): Boolean {
        return true
    }

    override fun onDialogDismiss(extraData: Bundle) {
    }

    override fun onPermissionsResult(permissions: ArrayList<String>, granted: Boolean, extraData: Bundle) {
        if (granted) {
            onPermissionsGranted()
        } else {
            finish()
        }
    }

    private fun onPermissionsGranted() {
        try {
            if (!CameraHelper.instance.isValid()) throw RuntimeException()
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

    override fun onNewVersion(oldVersion: Int, newVersion: Int) {
        super.onNewVersion(oldVersion, newVersion)
        when (oldVersion) {
            in 0..19999 -> {
                val sp = SharedPreferencesHelper.get("_profile_default")
                if (sp.get("ratio", "capture") == "raw") {
                    sp.edit {
                        remove("ratio")
                    }
                }
            }
        }
    }

    override val isBackFinishEnabled: Boolean = false

    companion object {
        private val permissions: ArrayList<String> = arrayListOf(Manifest.permission.CAMERA)
    }
}
