package com.ebnbin.eb.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
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

//*********************************************************************************************************************

/**
 * 将 parentFragment 或 activity 强转为 callback 接口.
 *
 * @param fragmentCallbackCast 按照什么顺序强转 Callback 接口.
 */
inline fun <reified T> Fragment.getCallback(
    fragmentCallbackCast: FragmentCallbackCast = FragmentCallbackCast.PREFER_PARENT_FRAGMENT
): T? {
    when (fragmentCallbackCast) {
        FragmentCallbackCast.PREFER_PARENT_FRAGMENT -> arrayOf(parentFragment, activity)
        FragmentCallbackCast.PREFER_ACTIVITY -> arrayOf(activity, parentFragment)
        FragmentCallbackCast.PARENT_FRAGMENT -> arrayOf(parentFragment)
        FragmentCallbackCast.ACTIVITY -> arrayOf(activity)
    }.forEach {
        if (T::class.java.isInstance(it)) {
            return T::class.java.cast(it)
        }
    }
    return null
}

/**
 * 将 parentFragment 或 activity 强转为 callback 接口. 如果强转失败则抛出异常.
 */
inline fun <reified T> Fragment.requireCallback(
    fragmentCallbackCast: FragmentCallbackCast = FragmentCallbackCast.PREFER_PARENT_FRAGMENT
): T {
    return getCallback<T>(fragmentCallbackCast)!!
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

fun Fragment.removeSelf(allowStateLoss: Boolean = true) {
    parentFragmentManager.commit(allowStateLoss) {
        remove(this@removeSelf)
    }
}
