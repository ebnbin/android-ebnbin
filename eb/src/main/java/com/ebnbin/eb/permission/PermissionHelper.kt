package com.ebnbin.eb.permission

import android.Manifest
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.ebnbin.eb.util.ebApp

/**
 * 权限帮助类.
 */
object PermissionHelper {
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
                Manifest.permission.SYSTEM_ALERT_WINDOW -> isSystemAlertWindowPermissionGranted()
                else -> isRuntimePermissionGranted(it)
            }
        }
    }
}
