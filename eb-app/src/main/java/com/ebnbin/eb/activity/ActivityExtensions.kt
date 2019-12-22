package com.ebnbin.eb.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment

fun Context.openActivity(intent: Intent, options: Bundle? = null): Throwable? {
    return runCatching {
        startActivity(intent, options)
    }.exceptionOrNull()
}

fun Activity.openActivity(intent: Intent, requestCode: Int = 0, options: Bundle? = null): Throwable? {
    return runCatching {
        startActivityForResult(intent, requestCode, options)
    }.exceptionOrNull()
}

fun Fragment.openActivity(intent: Intent, requestCode: Int = 0, options: Bundle? = null): Throwable? {
    return runCatching {
        startActivityForResult(intent, requestCode, options)
    }.exceptionOrNull()
}

//*********************************************************************************************************************

fun Context.openActivity(
    activityClass: Class<out Activity>,
    options: Bundle? = null
): Throwable? {
    return runCatching {
        startActivity(Intent(this, activityClass), options)
    }.exceptionOrNull()
}

fun Activity.openActivity(
    activityClass: Class<out Activity>,
    requestCode: Int = 0,
    options: Bundle? = null
): Throwable? {
    return runCatching {
        startActivityForResult(Intent(this, activityClass), requestCode, options)
    }.exceptionOrNull()
}

fun Fragment.openActivity(
    activityClass: Class<out Activity>,
    requestCode: Int = 0,
    options: Bundle? = null
): Throwable? {
    return runCatching {
        startActivityForResult(Intent(requireContext(), activityClass), requestCode, options)
    }.exceptionOrNull()
}

//*********************************************************************************************************************

inline fun <reified T : Activity> Context.openActivity(options: Bundle? = null): Throwable? {
    return openActivity(
        activityClass = T::class.java,
        options = options
    )
}

inline fun <reified T : Activity> Activity.openActivity(requestCode: Int = 0, options: Bundle? = null): Throwable? {
    return openActivity(
        activityClass = T::class.java,
        requestCode = requestCode,
        options = options
    )
}

inline fun <reified T : Activity> Fragment.openActivity(requestCode: Int = 0, options: Bundle? = null): Throwable? {
    return openActivity(
        activityClass = T::class.java,
        requestCode = requestCode,
        options = options
    )
}

//*********************************************************************************************************************

fun Activity.containsExtra(key: String): Boolean {
    return intent?.extras?.containsKey(key) ?: false
}

inline fun <reified T> Activity.getExtra(key: String): T? {
    return intent?.extras?.get(key) as T?
}

/**
 * 如果 key 不存在则抛出异常.
 */
inline fun <reified T> Activity.requireExtra(key: String): T {
    return intent?.extras?.get(key) as T
}

/**
 * 如果 key 不存在则返回默认值.
 */
inline fun <reified T> Activity.getExtraOrDefault(key: String, defaultValue: T): T {
    return if (containsExtra(key)) requireExtra(key) else defaultValue
}
