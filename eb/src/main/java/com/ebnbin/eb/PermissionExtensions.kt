package com.ebnbin.eb

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.provider.Settings

fun Context.hasRequestInstallPackagesPermission(): Boolean =
    if (sdk26O()) packageManager.canRequestPackageInstalls() else true

fun Context.hasSystemAlertWindowPermission(): Boolean = Settings.canDrawOverlays(this)

fun Context.hasWriteSettingsPermission(): Boolean = Settings.System.canWrite(this)

fun Context.hasRuntimePermission(runtimePermission: String): Boolean =
    checkSelfPermission(runtimePermission) == PackageManager.PERMISSION_GRANTED

fun Context.hasPermissions(vararg permissions: String): Boolean =
    permissions.all {
        when (it) {
            Manifest.permission.REQUEST_INSTALL_PACKAGES -> hasRequestInstallPackagesPermission()
            Manifest.permission.SYSTEM_ALERT_WINDOW -> hasSystemAlertWindowPermission()
            Manifest.permission.WRITE_SETTINGS -> hasWriteSettingsPermission()
            else -> hasRuntimePermission(it)
        }
    }
