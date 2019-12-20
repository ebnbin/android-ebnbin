package com.ebnbin.eb.extension

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.ebnbin.eb.EBApp
import com.ebnbin.eb.dialog.AlertDialogFragment
import com.ebnbin.eb.permission.PermissionFragment

fun FragmentManager.openAlertDialog(builder: AlertDialogFragment.Builder, tag: String? = null) {
    beginTransaction()
        .add(AlertDialogFragment::class.java, bundleOf(AlertDialogFragment.KEY_BUILDER to builder), tag)
        .commitAllowingStateLoss()
}

//*********************************************************************************************************************

fun FragmentManager.openPermissionFragment(
    permissions: Array<out String>,
    extraData: Bundle = Bundle.EMPTY,
    tag: String? = null
) {
    beginTransaction()
        .add(PermissionFragment::class.java, bundleOf(
            PermissionFragment.KEY_PERMISSIONS to permissions,
            PermissionFragment.KEY_EXTRA_DATA to extraData
        ), tag)
        .commitAllowingStateLoss()
}

//*********************************************************************************************************************

internal fun Context.hasRequestInstallPackagesPermission(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) packageManager.canRequestPackageInstalls() else true
}

internal fun Context.hasSystemAlertWindowPermission(): Boolean {
    return Settings.canDrawOverlays(this)
}

internal fun Context.hasRuntimePermission(runtimePermission: String): Boolean {
    return checkSelfPermission(runtimePermission) == PackageManager.PERMISSION_GRANTED
}

fun Context.hasPermissions(permissions: Array<out String>): Boolean {
    return permissions.all {
        when (it) {
            Manifest.permission.REQUEST_INSTALL_PACKAGES -> hasRequestInstallPackagesPermission()
            Manifest.permission.SYSTEM_ALERT_WINDOW -> hasSystemAlertWindowPermission()
            else -> hasRuntimePermission(it)
        }
    }
}

internal fun Fragment.hasRequestInstallPackagesPermission(): Boolean {
    return context?.hasRequestInstallPackagesPermission() ?: false
}

internal fun Fragment.hasSystemAlertWindowPermission(): Boolean {
    return context?.hasSystemAlertWindowPermission() ?: false
}

internal fun Fragment.hasRuntimePermission(runtimePermission: String): Boolean {
    return context?.hasRuntimePermission(runtimePermission) ?: false
}

fun Fragment.hasPermissions(permissions: Array<out String>): Boolean {
    return context?.hasPermissions(permissions) ?: false
}
//*********************************************************************************************************************

fun Context.toast(@StringRes resId: Int, durationLong: Boolean = false) {
    val toast = Toast.makeText(this, resId, if (durationLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
    EBApp.instance.setToast(toast)
    toast.show()
}

fun Context.toast(text: CharSequence, durationLong: Boolean = false) {
    val toast = Toast.makeText(this, text, if (durationLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
    EBApp.instance.setToast(toast)
    toast.show()
}
