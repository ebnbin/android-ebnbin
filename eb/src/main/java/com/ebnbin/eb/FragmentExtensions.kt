package com.ebnbin.eb

import androidx.fragment.app.Fragment

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
