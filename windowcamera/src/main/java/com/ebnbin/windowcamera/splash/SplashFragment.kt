package com.ebnbin.windowcamera.splash

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
import androidx.core.os.bundleOf
import com.ebnbin.eb.dialog.AlertDialogFragment
import com.ebnbin.eb.dialog.DialogCancelable
import com.ebnbin.eb.extension.openAlertDialog
import com.ebnbin.eb2.dev.DevHelper
import com.ebnbin.eb2.dev.EBReport
import com.ebnbin.eb2.sharedpreferences.SharedPreferencesHelper
import com.ebnbin.eb2.splash.EBSplashFragment
import com.ebnbin.eb2.util.Consts
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
                finish()
                return
            } else {
                throw CameraInvalidException(CameraHelper.getInvalidString())
            }
        } catch (throwable: Throwable) {
            DevHelper.reportThrowable(throwable)
        }
        childFragmentManager.openAlertDialog(AlertDialogFragment.Builder(
            message = getString(R.string.splash_camera_message),
            positiveButtonText = getString(R.string.splash_camera_positive),
            dialogCancelable = DialogCancelable.NOT_CANCELABLE,
            extraData = bundleOf(Consts.KEY_CALLING_ID to "splash_camera")
        ), "splash_camera")
    }

    override fun createReport(): EBReport {
        return Report().create()
    }

    override fun alertDialogOnPositive(alertDialog: AlertDialog, extraData: Bundle): Boolean {
        when (extraData.getString(Consts.KEY_CALLING_ID)) {
            "splash_camera" -> {
                finish()
                return true
            }
            else -> return super.alertDialogOnPositive(alertDialog, extraData)
        }
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
