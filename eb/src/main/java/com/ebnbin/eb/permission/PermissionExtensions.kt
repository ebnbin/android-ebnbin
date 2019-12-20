package com.ebnbin.eb.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.ebnbin.eb.util.sdk26O

fun Context.hasRequestInstallPackagesPermission(): Boolean {
    return if (sdk26O()) packageManager.canRequestPackageInstalls() else true
}

fun Context.hasSystemAlertWindowPermission(): Boolean {
    return Settings.canDrawOverlays(this)
}

fun Context.hasWriteSettingsPermission(): Boolean {
    return Settings.System.canWrite(this)
}

fun Context.hasRuntimePermission(runtimePermission: String): Boolean {
    return checkSelfPermission(runtimePermission) == PackageManager.PERMISSION_GRANTED
}

fun Context.hasPermissions(vararg permissions: String): Boolean {
    return permissions.all {
        when (it) {
            Manifest.permission.REQUEST_INSTALL_PACKAGES -> hasRequestInstallPackagesPermission()
            Manifest.permission.SYSTEM_ALERT_WINDOW -> hasSystemAlertWindowPermission()
            Manifest.permission.WRITE_SETTINGS -> hasWriteSettingsPermission()
            else -> hasRuntimePermission(it)
        }
    }
}

//*********************************************************************************************************************

fun FragmentManager.openPermissionFragment(
    permissions: Array<out String>,
    callbackBundle: Bundle = Bundle.EMPTY,
    fragmentTag: String? = null
) {
    commit(true) {
        add(PermissionFragment::class.java, PermissionFragment.createArguments(
            permissions = permissions,
            callbackBundle = callbackBundle
        ), fragmentTag)
    }
}
