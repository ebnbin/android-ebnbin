package com.ebnbin.windowcamera.splash

import android.Manifest
import android.os.Bundle
import android.os.Environment
import androidx.core.content.edit
import androidx.core.os.bundleOf
import com.ebnbin.eb.dev.DevHelper
import com.ebnbin.eb.dev.EBReport
import com.ebnbin.eb.dialog.DialogCancel
import com.ebnbin.eb.dialog.SimpleDialogFragment
import com.ebnbin.eb.permission.PermissionHelper
import com.ebnbin.eb.sharedpreferences.SharedPreferencesHelper
import com.ebnbin.eb.splash.EBSplashFragment
import com.ebnbin.eb.util.BuildHelper
import com.ebnbin.eb.util.Consts
import com.ebnbin.eb.util.IntentHelper
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.camera.CameraHelper
import com.ebnbin.windowcamera.camera.exception.CameraInvalidException
import com.ebnbin.windowcamera.dev.Report
import com.ebnbin.windowcamera.main.MainActivity
import com.ebnbin.windowcamera.util.IOHelper
import com.ebnbin.windowcamera.util.SpManager
import java.io.File

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

    override fun createReport(): EBReport {
        return Report().create()
    }

    override fun onDialogPositive(extraData: Bundle): Boolean {
        when (extraData.getString(Consts.KEY_CALLING_ID)) {
            "splash_camera" -> {
                finish()
                return true
            }
            else -> return super.onDialogPositive(extraData)
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
        if (oldVersion < 60000) {
            v60000Path()
        }
    }

    private fun v60000Path() {
        if (SpManager.v60000_path.value) return
        SpManager.v60000_path.value = true
        if (!PermissionHelper.isPermissionsGranted(listOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE))) return
        @Suppress("DEPRECATION")
        val externalStorageDirectory = Environment.getExternalStorageDirectory() ?: return
        val oldPath = File(externalStorageDirectory, "${BuildHelper.applicationId}/${Environment.DIRECTORY_DCIM}/")
        if (!oldPath.exists()) return
        val newPath = IOHelper.getPath() ?: return
        oldPath
            .listFiles { _, name ->
                when (name.substringAfterLast(".")) {
                    "jpg", "mp4", "3gp" -> true
                    else -> false
                }
            }
            ?.toSortedSet(Comparator { file1, file2 ->
                compareValuesBy(file2, file1, File::lastModified, File::getName)
            })
            ?.forEach {
                it.renameTo(File(newPath, it.name))
            }
    }

    override val isBackFinishEnabled: Boolean = false
}
