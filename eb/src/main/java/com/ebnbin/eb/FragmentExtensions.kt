package com.ebnbin.eb

import androidx.fragment.app.Fragment

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
    fragmentCallbackCast: FragmentCallbackCast = FragmentCallbackCast.PREFER_PARENT_FRAGMENT
): T? = when (fragmentCallbackCast) {
    FragmentCallbackCast.PREFER_PARENT_FRAGMENT -> arrayOf(parentFragment, activity)
    FragmentCallbackCast.PREFER_ACTIVITY -> arrayOf(activity, parentFragment)
    FragmentCallbackCast.PARENT_FRAGMENT -> arrayOf(parentFragment)
    FragmentCallbackCast.ACTIVITY -> arrayOf(activity)
}.firstOrNull {
    it is T
} as T?

inline fun <reified T> Fragment.requireCallback(
    fragmentCallbackCast: FragmentCallbackCast = FragmentCallbackCast.PREFER_PARENT_FRAGMENT
): T = getCallback<T>(fragmentCallbackCast) ?: throw ClassCastException("无法通过强转获取 Callback 接口.")
