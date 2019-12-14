package com.ebnbin.eb2.permission

import android.Manifest
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.ebnbin.eb2.util.BuildHelper
import com.ebnbin.eb2.util.ebApp

/**
 * 权限帮助类.
 */
object PermissionHelper {
    internal fun isRequestInstallPackagesPermissionGranted(): Boolean {
        return if (BuildHelper.sdk26O()) ebApp.packageManager.canRequestPackageInstalls() else true
    }

    internal fun isSystemAlertWindowPermissionGranted(): Boolean {
        return Settings.canDrawOverlays(ebApp)
    }

    internal fun isRuntimePermissionGranted(runtimePermission: String): Boolean {
        return ContextCompat.checkSelfPermission(ebApp, runtimePermission) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 检查权限是否全部允许.
     */
    fun isPermissionsGranted(permissions: List<String>): Boolean {
        return LinkedHashSet(permissions).all {
            when (it) {
                Manifest.permission.REQUEST_INSTALL_PACKAGES -> isRequestInstallPackagesPermissionGranted()
                Manifest.permission.SYSTEM_ALERT_WINDOW -> isSystemAlertWindowPermissionGranted()
                else -> isRuntimePermissionGranted(it)
            }
        }
    }
}
