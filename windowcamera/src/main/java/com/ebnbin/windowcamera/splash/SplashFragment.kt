package com.ebnbin.windowcamera.splash

import android.os.Bundle
import androidx.core.content.edit
import androidx.core.os.bundleOf
import com.ebnbin.eb.dev.DevHelper
import com.ebnbin.eb.dialog.DialogCancel
import com.ebnbin.eb.dialog.SimpleDialogFragment
import com.ebnbin.eb.sharedpreferences.SharedPreferencesHelper
import com.ebnbin.eb.splash.EBSplashFragment
import com.ebnbin.eb.util.Consts
import com.ebnbin.eb.util.IntentHelper
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.camera.CameraHelper
import com.ebnbin.windowcamera.camera.exception.CameraInvalidException
import com.ebnbin.windowcamera.dev.Report
import com.ebnbin.windowcamera.main.MainActivity

class SplashFragment : EBSplashFragment(), SimpleDialogFragment.Callback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            CameraHelper
            DevHelper.report { Report().create() }
            if (CameraHelper.isValid()) {
                IntentHelper.startActivityFromFragment(this, MainActivity::class.java)
                finish()
                return
            } else {
                throw CameraInvalidException()
            }
        } catch (throwable: Throwable) {
            DevHelper.report(throwable)
        }
        SimpleDialogFragment.start(
            childFragmentManager,
            SimpleDialogFragment.Builder(
                message = getString(R.string.splash_camera_message),
                positive = getString(R.string.splash_camera_positive),
                dialogCancel = DialogCancel.NOT_CANCELABLE
            ),
            "splash_camera",
            bundleOf(
                Consts.KEY_CALLING_ID to "splash_camera"
            )
        )
    }

    override fun onDialogPositive(extraData: Bundle): Boolean {
        when (extraData.getString(Consts.KEY_CALLING_ID)) {
            "splash_camera" -> {
                finish()
                return true
            }
            else -> {
                return true
            }
        }
    }

    override fun onDialogNegative(extraData: Bundle): Boolean {
        return true
    }

    override fun onDialogNeutral(extraData: Bundle): Boolean {
        return true
    }

    override fun onDialogDismiss(extraData: Bundle) {
    }

    override fun onNewVersion(oldVersion: Int, newVersion: Int) {
        super.onNewVersion(oldVersion, newVersion)
        if (oldVersion < 40000) {
            val sp = SharedPreferencesHelper.get("_profile_default")
            sp.edit {
                clear()
            }
        }
    }

    override val isBackFinishEnabled: Boolean = false
}
