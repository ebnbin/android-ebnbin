package com.ebnbin.eb

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit

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

/**
 * 返回需要请求的权限, 去重.
 */
fun Context.getNeededPermissions(vararg permissions: String): Set<String> {
    val permissionSet = LinkedHashSet(permissions.toList())
    permissionSet.toList().forEach {
        if (hasPermissions(it)) permissionSet.remove(it)
    }
    return permissionSet
}

//*********************************************************************************************************************

private val PERMISSION_NAME_IDS: Map<String, Int> = mapOf(
    Manifest.permission.SYSTEM_ALERT_WINDOW to R.string.eb_permission_name_system_alert_window,
    Manifest.permission.WRITE_SETTINGS to R.string.eb_permission_name_write_settings,
    Manifest.permission.REQUEST_INSTALL_PACKAGES to R.string.eb_permission_name_request_install_packages,
    Manifest.permission.READ_EXTERNAL_STORAGE to R.string.eb_permission_name_read_external_storage,
    Manifest.permission.WRITE_EXTERNAL_STORAGE to R.string.eb_permission_name_write_external_storage,
    Manifest.permission.RECORD_AUDIO to R.string.eb_permission_name_record_audio,
    Manifest.permission.CAMERA to R.string.eb_permission_name_camera
)

fun Context.getPermissionName(permission: String, defaultValue: String = permission): String {
    return PERMISSION_NAME_IDS[permission]?.let { getString(it) } ?: defaultValue
}

//*********************************************************************************************************************

fun FragmentManager.openPermissionFragment(
    permissions: Array<out String>,
    fragmentCallback: FragmentCallback = FragmentCallback.PREFER_PARENT_FRAGMENT,
    callbackBundle: Bundle = Bundle.EMPTY,
    fragmentTag: String? = null
) {
    commit(true) {
        add(
            PermissionFragment::class.java,
            PermissionFragment.createArguments(
                permissions = permissions,
                fragmentCallback = fragmentCallback,
                callbackBundle = callbackBundle
            ),
            fragmentTag
        )
    }
}
