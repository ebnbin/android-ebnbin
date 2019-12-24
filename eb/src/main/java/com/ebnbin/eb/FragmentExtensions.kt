package com.ebnbin.eb

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit

/**
 * 返回 argument 是否存在.
 */
fun Fragment.containsArgument(key: String): Boolean {
    return arguments?.containsKey(key) ?: false
}

/**
 * 返回 argument 或 null.
 */
inline fun <reified T> Fragment.getArgument(key: String): T? {
    return arguments?.get(key) as T?
}

/**
 * 返回 argument 或抛出 [NullPointerException].
 */
inline fun <reified T> Fragment.requireArgument(key: String): T {
    return arguments?.get(key) as T
}

/**
 * 如果 [key] 不存在则返回 [defaultValue], 否则返回 argument 或抛出 [NullPointerException].
 */
inline fun <reified T> Fragment.getArgumentOrDefault(key: String, defaultValue: T): T {
    return if (containsArgument(key)) requireArgument(key) else defaultValue
}

//*********************************************************************************************************************

/**
 * 将 parentFragment 或 activity 强转为 Callback 接口.
 */
inline fun <reified T> Fragment.getCallback(
    fragmentCallback: FragmentCallback = FragmentCallback.PREFER_PARENT_FRAGMENT
): T? {
    return when (fragmentCallback) {
        FragmentCallback.PREFER_PARENT_FRAGMENT -> arrayOf(parentFragment, activity)
        FragmentCallback.PREFER_ACTIVITY -> arrayOf(activity, parentFragment)
        FragmentCallback.PARENT_FRAGMENT -> arrayOf(parentFragment)
        FragmentCallback.ACTIVITY -> arrayOf(activity)
    }.firstOrNull {
        it is T
    } as T?
}

inline fun <reified T> Fragment.requireCallback(
    fragmentCallback: FragmentCallback = FragmentCallback.PREFER_PARENT_FRAGMENT
): T {
    return getCallback<T>(fragmentCallback) ?: throw ClassCastException("无法通过强转获取 Callback 接口.")
}

//*********************************************************************************************************************

fun Context.openFragment(
    fragmentClass: Class<out Fragment>,
    fragmentArguments: Bundle? = null,
    fragmentTag: String? = null,
    fragmentIsView: Boolean = true,
    fragmentPermissions: Array<out String>? = null,
    activityIntent: Intent? = null,
    activityOptions: Bundle? = null
) {
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
) {
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
) {
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
}

//*********************************************************************************************************************

inline fun <reified T : Fragment> Context.openFragment(
    fragmentArguments: Bundle? = null,
    fragmentTag: String? = null,
    fragmentIsView: Boolean = true,
    fragmentPermissions: Array<out String>? = null,
    activityIntent: Intent? = null,
    activityOptions: Bundle? = null
) {
    openFragment(
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
) {
    openFragment(
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
) {
    openFragment(
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

fun Fragment.removeSelf() {
    parentFragmentManager.commit(true) {
        remove(this@removeSelf)
    }
}
