package com.ebnbin.eb.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ebnbin.eb.activity.EBFragmentActivity

fun Context.openFragment(
    fragmentClass: Class<out Fragment>,
    fragmentArgs: Bundle? = null,
    fragmentTag: String? = null,
    fragmentIsView: Boolean = true,
    fragmentPermissions: Array<out String>? = null,
    activityIntent: Intent? = null,
    activityOptions: Bundle? = null
): Throwable? {
    return runCatching {
        startActivity(
            EBFragmentActivity.createIntent(
                context = this,
                fragmentClass = fragmentClass,
                fragmentArgs = fragmentArgs,
                fragmentTag = fragmentTag,
                fragmentIsView = fragmentIsView,
                fragmentPermissions = fragmentPermissions,
                activityIntent = activityIntent
            ),
            activityOptions
        )
    }.exceptionOrNull()
}

fun Activity.openFragment(
    fragmentClass: Class<out Fragment>,
    fragmentArgs: Bundle? = null,
    fragmentTag: String? = null,
    fragmentIsView: Boolean = true,
    fragmentPermissions: Array<out String>? = null,
    activityIntent: Intent? = null,
    activityRequestCode: Int = 0,
    activityOptions: Bundle? = null
): Throwable? {
    return runCatching {
        startActivityForResult(
            EBFragmentActivity.createIntent(
                context = this,
                fragmentClass = fragmentClass,
                fragmentArgs = fragmentArgs,
                fragmentTag = fragmentTag,
                fragmentIsView = fragmentIsView,
                fragmentPermissions = fragmentPermissions,
                activityIntent = activityIntent
            ),
            activityRequestCode,
            activityOptions
        )
    }.exceptionOrNull()
}

fun Fragment.openFragment(
    fragmentClass: Class<out Fragment>,
    fragmentArgs: Bundle? = null,
    fragmentTag: String? = null,
    fragmentIsView: Boolean = true,
    fragmentPermissions: Array<out String>? = null,
    activityIntent: Intent? = null,
    activityRequestCode: Int = 0,
    activityOptions: Bundle? = null
): Throwable? {
    return runCatching {
        startActivityForResult(
            EBFragmentActivity.createIntent(
                context = requireContext(),
                fragmentClass = fragmentClass,
                fragmentArgs = fragmentArgs,
                fragmentTag = fragmentTag,
                fragmentIsView = fragmentIsView,
                fragmentPermissions = fragmentPermissions,
                activityIntent = activityIntent
            ),
            activityRequestCode,
            activityOptions
        )
    }.exceptionOrNull()
}

//*********************************************************************************************************************

inline fun <reified T : Fragment> Context.openFragment(
    fragmentArgs: Bundle? = null,
    fragmentTag: String? = null,
    fragmentIsView: Boolean = true,
    fragmentPermissions: Array<out String>? = null,
    activityIntent: Intent? = null,
    activityOptions: Bundle? = null
): Throwable? {
    return openFragment(
        fragmentClass = T::class.java,
        fragmentArgs = fragmentArgs,
        fragmentTag = fragmentTag,
        fragmentIsView = fragmentIsView,
        fragmentPermissions = fragmentPermissions,
        activityIntent = activityIntent,
        activityOptions = activityOptions
    )
}

inline fun <reified T : Fragment> Activity.openFragment(
    fragmentArgs: Bundle? = null,
    fragmentTag: String? = null,
    fragmentIsView: Boolean = true,
    fragmentPermissions: Array<out String>? = null,
    activityIntent: Intent? = null,
    activityRequestCode: Int = 0,
    activityOptions: Bundle? = null
): Throwable? {
    return openFragment(
        fragmentClass = T::class.java,
        fragmentArgs = fragmentArgs,
        fragmentTag = fragmentTag,
        fragmentIsView = fragmentIsView,
        fragmentPermissions = fragmentPermissions,
        activityIntent = activityIntent,
        activityRequestCode = activityRequestCode,
        activityOptions = activityOptions
    )
}

inline fun <reified T : Fragment> Fragment.openFragment(
    fragmentArgs: Bundle? = null,
    fragmentTag: String? = null,
    fragmentIsView: Boolean = true,
    fragmentPermissions: Array<out String>? = null,
    activityIntent: Intent? = null,
    activityRequestCode: Int = 0,
    activityOptions: Bundle? = null
): Throwable? {
    return openFragment(
        fragmentClass = T::class.java,
        fragmentArgs = fragmentArgs,
        fragmentTag = fragmentTag,
        fragmentIsView = fragmentIsView,
        fragmentPermissions = fragmentPermissions,
        activityIntent = activityIntent,
        activityRequestCode = activityRequestCode,
        activityOptions = activityOptions
    )
}
