package com.ebnbin.eb.extension

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.getSystemService
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.ebnbin.eb.EBApp
import com.ebnbin.eb.activity.EBFragmentActivity
import com.ebnbin.eb.dialog.AlertDialogFragment
import com.ebnbin.eb.permission.PermissionFragment
import kotlin.math.roundToInt

fun Context.dpToPx(dp: Float): Float {
    return dp * resources.displayMetrics.density
}

fun Context.dpToPxRound(dp: Float): Int {
    return dpToPx(dp).roundToInt()
}

fun Context.spToPx(sp: Float): Float {
    return sp * resources.displayMetrics.scaledDensity
}

fun Context.spToPxRound(sp: Float): Int {
    return spToPx(sp).roundToInt()
}

fun Context.pxToDp(px: Float): Float {
    return px / resources.displayMetrics.density
}

fun Context.pxToDp(px: Int): Float {
    return pxToDp(px.toFloat())
}

fun Context.pxToSp(px: Float): Float {
    return px / resources.displayMetrics.scaledDensity
}

fun Context.pxToSp(px: Int): Float {
    return pxToSp(px.toFloat())
}

//*********************************************************************************************************************

private fun createOpenActivityIntent(
    context: Context,
    intent: Intent?,
    activityClass: Class<out Activity>?
): Intent {
    return (if (intent == null) Intent() else Intent(intent)).also {
        if (activityClass != null) it.setClass(context, activityClass)
    }
}

fun Context.openActivity(
    intent: Intent,
    activityClass: Class<out Activity>? = null,
    options: Bundle? = null
): Boolean {
    return runCatching {
        startActivity(
            createOpenActivityIntent(this, intent, activityClass),
            options
        )
    }.isSuccess
}

fun Context.openActivity(
    activityClass: Class<out Activity>,
    options: Bundle? = null
): Boolean {
    return runCatching {
        startActivity(
            createOpenActivityIntent(this, null, activityClass),
            options
        )
    }.isSuccess
}

fun Activity.openActivity(
    intent: Intent,
    activityClass: Class<out Activity>? = null,
    requestCode: Int = 0,
    options: Bundle? = null
): Boolean {
    return runCatching {
        startActivityForResult(
            createOpenActivityIntent(this, intent, activityClass),
            requestCode,
            options
        )
    }.isSuccess
}

fun Activity.openActivity(
    activityClass: Class<out Activity>,
    requestCode: Int = 0,
    options: Bundle? = null
): Boolean {
    return runCatching {
        startActivityForResult(
            createOpenActivityIntent(this, null, activityClass),
            requestCode,
            options
        )
    }.isSuccess
}

fun Fragment.openActivity(
    intent: Intent,
    activityClass: Class<out Activity>? = null,
    requestCode: Int = 0,
    options: Bundle? = null
): Boolean {
    val context = context ?: return false
    return runCatching {
        startActivityForResult(
            createOpenActivityIntent(context, intent, activityClass),
            requestCode,
            options
        )
    }.isSuccess
}

fun Fragment.openActivity(
    activityClass: Class<out Activity>,
    requestCode: Int = 0,
    options: Bundle? = null
): Boolean {
    val context = context ?: return false
    return runCatching {
        startActivityForResult(
            createOpenActivityIntent(context, null, activityClass),
            requestCode,
            options
        )
    }.isSuccess
}

//*********************************************************************************************************************

private fun createOpenFragmentIntent(
    context: Context,
    fragmentClass: Class<out Fragment>,
    fragmentArgs: Bundle?,
    fragmentTag: String?,
    fragmentIsView: Boolean,
    activityIntent: Intent?
): Intent {
    return (if (activityIntent == null) Intent() else Intent(activityIntent)).also {
        it.setClass(context, EBFragmentActivity::class.java)
        it.putExtra(EBFragmentActivity.KEY_FRAGMENT_CLASS, fragmentClass)
        if (fragmentArgs != null) it.putExtra(EBFragmentActivity.KEY_FRAGMENT_ARGS, fragmentArgs)
        if (fragmentTag != null) it.putExtra(EBFragmentActivity.KEY_FRAGMENT_TAG, fragmentTag)
        it.putExtra(EBFragmentActivity.KEY_FRAGMENT_IS_VIEW, fragmentIsView)
    }
}

fun Context.openFragment(
    fragmentClass: Class<out Fragment>,
    fragmentArgs: Bundle? = null,
    fragmentTag: String? = null,
    fragmentIsView: Boolean = true,
    activityIntent: Intent? = null,
    activityOptions: Bundle? = null
): Boolean {
    return runCatching {
        startActivity(
            createOpenFragmentIntent(this, fragmentClass, fragmentArgs, fragmentTag, fragmentIsView, activityIntent),
            activityOptions
        )
    }.isSuccess
}

fun Activity.openFragment(
    fragmentClass: Class<out Fragment>,
    fragmentArgs: Bundle? = null,
    fragmentTag: String? = null,
    fragmentIsView: Boolean = true,
    activityIntent: Intent? = null,
    activityRequestCode: Int = 0,
    activityOptions: Bundle? = null
): Boolean {
    return runCatching {
        startActivityForResult(
            createOpenFragmentIntent(this, fragmentClass, fragmentArgs, fragmentTag, fragmentIsView, activityIntent),
            activityRequestCode,
            activityOptions
        )
    }.isSuccess
}

fun Fragment.openFragment(
    fragmentClass: Class<out Fragment>,
    fragmentArgs: Bundle? = null,
    fragmentTag: String? = null,
    fragmentIsView: Boolean = true,
    activityIntent: Intent? = null,
    activityRequestCode: Int = 0,
    activityOptions: Bundle? = null
): Boolean {
    val context = context ?: return false
    return runCatching {
        startActivityForResult(
            createOpenFragmentIntent(context, fragmentClass, fragmentArgs, fragmentTag, fragmentIsView,
                activityIntent),
            activityRequestCode,
            activityOptions
        )
    }.isSuccess
}

//*********************************************************************************************************************

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

fun Fragment.removeSelf() {
    parentFragmentManager.beginTransaction()
        .remove(this)
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

inline fun <reified T : Any> Context.requireSystemService(): T {
    return getSystemService() ?: throw RuntimeException()
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
