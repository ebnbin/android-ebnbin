package com.ebnbin.eb

import androidx.fragment.app.Fragment
import androidx.fragment.app.commit

/**
 * 返回 argument 是否存在.
 */
fun Fragment.containsArgument(key: String): Boolean = arguments?.containsKey(key) ?: false

/**
 * 返回 argument 或 null.
 */
inline fun <reified T> Fragment.getArgument(key: String): T? = arguments?.get(key) as T?

/**
 * 返回 argument 或抛出 [NullPointerException].
 */
inline fun <reified T> Fragment.requireArgument(key: String): T = arguments?.get(key) as T

/**
 * 如果 [key] 不存在则返回 [defaultValue], 否则返回 argument 或抛出 [NullPointerException].
 */
inline fun <reified T> Fragment.getArgumentOrDefault(key: String, defaultValue: T): T =
    if (containsArgument(key)) requireArgument(key) else defaultValue

//*********************************************************************************************************************

/**
 * 将 parentFragment 或 activity 强转为 Callback 接口.
 */
inline fun <reified T> Fragment.getCallback(
    fragmentCallback: FragmentCallback = FragmentCallback.PREFER_PARENT_FRAGMENT
): T? = when (fragmentCallback) {
    FragmentCallback.PREFER_PARENT_FRAGMENT -> arrayOf(parentFragment, activity)
    FragmentCallback.PREFER_ACTIVITY -> arrayOf(activity, parentFragment)
    FragmentCallback.PARENT_FRAGMENT -> arrayOf(parentFragment)
    FragmentCallback.ACTIVITY -> arrayOf(activity)
}.firstOrNull {
    it is T
} as T?

inline fun <reified T> Fragment.requireCallback(
    fragmentCallback: FragmentCallback = FragmentCallback.PREFER_PARENT_FRAGMENT
): T = getCallback<T>(fragmentCallback) ?: throw ClassCastException("无法通过强转获取 Callback 接口.")

//*********************************************************************************************************************

fun Fragment.removeSelf(): Unit = parentFragmentManager.commit(true) {
    remove(this@removeSelf)
}
