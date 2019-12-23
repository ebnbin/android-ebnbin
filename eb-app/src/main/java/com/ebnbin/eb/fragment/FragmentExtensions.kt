package com.ebnbin.eb.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.ebnbin.eb.activity.FragmentActivity

fun Context.openFragment(
    fragmentClass: Class<out Fragment>,
    fragmentArguments: Bundle? = null,
    fragmentTag: String? = null,
    fragmentIsView: Boolean = true,
    fragmentPermissions: Array<out String>? = null,
    activityIntent: Intent? = null,
    activityOptions: Bundle? = null
): Throwable? {
    return runCatching {
        startActivity(
            FragmentActivity.createIntent(
                context = this,
                fragmentClass = fragmentClass,
                fragmentArguments = fragmentArguments,
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
    fragmentArguments: Bundle? = null,
    fragmentTag: String? = null,
    fragmentIsView: Boolean = true,
    fragmentPermissions: Array<out String>? = null,
    activityIntent: Intent? = null,
    activityRequestCode: Int = 0,
    activityOptions: Bundle? = null
): Throwable? {
    return runCatching {
        startActivityForResult(
            FragmentActivity.createIntent(
                context = this,
                fragmentClass = fragmentClass,
                fragmentArguments = fragmentArguments,
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
    fragmentArguments: Bundle? = null,
    fragmentTag: String? = null,
    fragmentIsView: Boolean = true,
    fragmentPermissions: Array<out String>? = null,
    activityIntent: Intent? = null,
    activityRequestCode: Int = 0,
    activityOptions: Bundle? = null
): Throwable? {
    return runCatching {
        startActivityForResult(
            FragmentActivity.createIntent(
                context = requireContext(),
                fragmentClass = fragmentClass,
                fragmentArguments = fragmentArguments,
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
    fragmentArguments: Bundle? = null,
    fragmentTag: String? = null,
    fragmentIsView: Boolean = true,
    fragmentPermissions: Array<out String>? = null,
    activityIntent: Intent? = null,
    activityOptions: Bundle? = null
): Throwable? {
    return openFragment(
        fragmentClass = T::class.java,
        fragmentArguments = fragmentArguments,
        fragmentTag = fragmentTag,
        fragmentIsView = fragmentIsView,
        fragmentPermissions = fragmentPermissions,
        activityIntent = activityIntent,
        activityOptions = activityOptions
    )
}

inline fun <reified T : Fragment> Activity.openFragment(
    fragmentArguments: Bundle? = null,
    fragmentTag: String? = null,
    fragmentIsView: Boolean = true,
    fragmentPermissions: Array<out String>? = null,
    activityIntent: Intent? = null,
    activityRequestCode: Int = 0,
    activityOptions: Bundle? = null
): Throwable? {
    return openFragment(
        fragmentClass = T::class.java,
        fragmentArguments = fragmentArguments,
        fragmentTag = fragmentTag,
        fragmentIsView = fragmentIsView,
        fragmentPermissions = fragmentPermissions,
        activityIntent = activityIntent,
        activityRequestCode = activityRequestCode,
        activityOptions = activityOptions
    )
}

inline fun <reified T : Fragment> Fragment.openFragment(
    fragmentArguments: Bundle? = null,
    fragmentTag: String? = null,
    fragmentIsView: Boolean = true,
    fragmentPermissions: Array<out String>? = null,
    activityIntent: Intent? = null,
    activityRequestCode: Int = 0,
    activityOptions: Bundle? = null
): Throwable? {
    return openFragment(
        fragmentClass = T::class.java,
        fragmentArguments = fragmentArguments,
        fragmentTag = fragmentTag,
        fragmentIsView = fragmentIsView,
        fragmentPermissions = fragmentPermissions,
        activityIntent = activityIntent,
        activityRequestCode = activityRequestCode,
        activityOptions = activityOptions
    )
}

//*********************************************************************************************************************

fun Fragment.containsArgument(key: String): Boolean {
    return arguments?.containsKey(key) ?: false
}

inline fun <reified T> Fragment.getArgument(key: String): T? {
    return arguments?.get(key) as T?
}

/**
 * 如果 key 不存在则抛出异常.
 */
inline fun <reified T> Fragment.requireArgument(key: String): T {
    return arguments?.get(key) as T
}

/**
 * 如果 key 不存在则返回默认值.
 */
inline fun <reified T> Fragment.getArgumentOrDefault(key: String, defaultValue: T): T {
    return if (containsArgument(key)) requireArgument(key) else defaultValue
}

//*********************************************************************************************************************

fun Fragment.removeSelf() {
    parentFragmentManager.commit(true) {
        remove(this@removeSelf)
    }
}
