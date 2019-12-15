package com.ebnbin.eb.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.ebnbin.eb.activity.EBFragmentActivity
import com.ebnbin.eb.dialog.AlertDialogFragment
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
) {
    startActivity(
        createOpenActivityIntent(this, intent, activityClass),
        options
    )
}

fun Context.openActivity(
    activityClass: Class<out Activity>,
    options: Bundle? = null
) {
    startActivity(
        createOpenActivityIntent(this, null, activityClass),
        options
    )
}

fun Activity.openActivity(
    intent: Intent,
    activityClass: Class<out Activity>? = null,
    requestCode: Int = 0,
    options: Bundle? = null
) {
    startActivityForResult(
        createOpenActivityIntent(this, intent, activityClass),
        requestCode,
        options
    )
}

fun Activity.openActivity(
    activityClass: Class<out Activity>,
    requestCode: Int = 0,
    options: Bundle? = null
) {
    startActivityForResult(
        createOpenActivityIntent(this, null, activityClass),
        requestCode,
        options
    )
}

fun Fragment.openActivity(
    intent: Intent,
    activityClass: Class<out Activity>? = null,
    requestCode: Int = 0,
    options: Bundle? = null
) {
    val context = context ?: return
    startActivityForResult(
        createOpenActivityIntent(context, intent, activityClass),
        requestCode,
        options
    )
}

fun Fragment.openActivity(
    activityClass: Class<out Activity>,
    requestCode: Int = 0,
    options: Bundle? = null
) {
    val context = context ?: return
    startActivityForResult(
        createOpenActivityIntent(context, null, activityClass),
        requestCode,
        options
    )
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
) {
    startActivity(
        createOpenFragmentIntent(this, fragmentClass, fragmentArgs, fragmentTag, fragmentIsView, activityIntent),
        activityOptions
    )
}

fun Activity.openFragment(
    fragmentClass: Class<out Fragment>,
    fragmentArgs: Bundle? = null,
    fragmentTag: String? = null,
    fragmentIsView: Boolean = true,
    activityIntent: Intent? = null,
    activityRequestCode: Int = 0,
    activityOptions: Bundle? = null
) {
    startActivityForResult(
        createOpenFragmentIntent(this, fragmentClass, fragmentArgs, fragmentTag, fragmentIsView, activityIntent),
        activityRequestCode,
        activityOptions
    )
}

fun Fragment.openFragment(
    fragmentClass: Class<out Fragment>,
    fragmentArgs: Bundle? = null,
    fragmentTag: String? = null,
    fragmentIsView: Boolean = true,
    activityIntent: Intent? = null,
    activityRequestCode: Int = 0,
    activityOptions: Bundle? = null
) {
    val context = context ?: return
    startActivityForResult(
        createOpenFragmentIntent(context, fragmentClass, fragmentArgs, fragmentTag, fragmentIsView, activityIntent),
        activityRequestCode,
        activityOptions
    )
}

//*********************************************************************************************************************

fun FragmentManager.openAlertDialog(builder: AlertDialogFragment.Builder, tag: String? = null) {
    beginTransaction()
        .add(AlertDialogFragment::class.java, bundleOf(AlertDialogFragment.KEY_BUILDER to builder), tag)
        .commitAllowingStateLoss()
}
