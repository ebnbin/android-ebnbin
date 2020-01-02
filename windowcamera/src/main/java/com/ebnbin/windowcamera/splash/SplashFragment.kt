package com.ebnbin.windowcamera.splash

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
import androidx.core.os.bundleOf
import com.ebnbin.eb.app2.EBApp
import com.ebnbin.eb.dialog.DialogCancelable
import com.ebnbin.eb.dialog.openAlertDialogFragment
import com.ebnbin.eb.sharedpreferences.getSharedPreferences
import com.ebnbin.eb2.dev.DevHelper
import com.ebnbin.eb2.dev.EBReport
import com.ebnbin.eb2.splash.EBSplashFragment
import com.ebnbin.eb2.util.IntentHelper
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.camera.CameraHelper
import com.ebnbin.windowcamera.camera.exception.CameraInvalidException
import com.ebnbin.windowcamera.dev.Report
import com.ebnbin.windowcamera.main.MainActivity

class SplashFragment : EBSplashFragment() {
    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        try {
            if (CameraHelper.isValid()) {
                IntentHelper.startActivityFromFragment(this, MainActivity::class.java)
                activity?.finish()
                return
            } else {
                throw CameraInvalidException(CameraHelper.getInvalidString())
            }
        } catch (throwable: Throwable) {
            DevHelper.reportThrowable(throwable)
        }
        childFragmentManager.openAlertDialogFragment(
            message = getString(R.string.splash_camera_message),
            positiveText = getString(R.string.splash_camera_positive),
            dialogCancelable = DialogCancelable.NOT_CANCELABLE,
            callbackBundle = bundleOf("calling_id" to "splash_camera"),
            fragmentTag = "splash_camera"
        )
    }

    override fun createReport(): EBReport {
        return Report().create()
    }

    override fun onAlertDialogPositive(alertDialog: AlertDialog, callbackBundle: Bundle): Boolean {
        return when (callbackBundle.getString("calling_id")) {
            "splash_camera" -> {
                activity?.finish()
                true
            }
            else -> super.onAlertDialogPositive(alertDialog, callbackBundle)
        }
    }

    override fun onNewVersion(oldVersion: Int, newVersion: Int) {
        super.onNewVersion(oldVersion, newVersion)
        if (oldVersion < 40000) {
            val sp = EBApp.instance.getSharedPreferences("_profile_default")
            sp.edit {
                clear()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        doubleBackFinishOnBackPressedCallback.isEnabled = true
    }
}
