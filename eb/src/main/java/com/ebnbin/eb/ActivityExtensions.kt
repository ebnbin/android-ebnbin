package com.ebnbin.eb

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * 返回 extra 是否存在.
 */
fun Activity.containsExtra(key: String): Boolean {
    return intent?.extras?.containsKey(key) ?: false
}

/**
 * 返回 extra 或 null.
 */
inline fun <reified T> Activity.getExtra(key: String): T? {
    return intent?.extras?.get(key) as T?
}

/**
 * 返回 extra 或抛出 [NullPointerException].
 */
inline fun <reified T> Activity.requireExtra(key: String): T {
    return intent?.extras?.get(key) as T
}

/**
 * 如果 [key] 不存在则返回 [defaultValue], 否则返回 extra 或抛出 [NullPointerException].
 */
inline fun <reified T> Activity.getExtraOrDefault(key: String, defaultValue: T): T {
    return if (containsExtra(key)) requireExtra(key) else defaultValue
}

//*********************************************************************************************************************

fun Context.openActivity(intent: Intent, options: Bundle? = null) {
    startActivity(intent, options)
}

fun Activity.openActivity(intent: Intent, requestCode: Int = 0, options: Bundle? = null) {
    startActivityForResult(intent, requestCode, options)
}

fun Fragment.openActivity(intent: Intent, requestCode: Int = 0, options: Bundle? = null) {
    startActivityForResult(intent, requestCode, options)
}

//*********************************************************************************************************************

fun Context.openActivity(activityClass: Class<out Activity>, options: Bundle? = null) {
    startActivity(Intent(this, activityClass), options)
}

fun Activity.openActivity(activityClass: Class<out Activity>, requestCode: Int = 0, options: Bundle? = null) {
    startActivityForResult(Intent(this, activityClass), requestCode, options)
}

fun Fragment.openActivity(activityClass: Class<out Activity>, requestCode: Int = 0, options: Bundle? = null) {
    startActivityForResult(Intent(requireContext(), activityClass), requestCode, options)
}

//*********************************************************************************************************************

inline fun <reified T : Activity> Context.openActivity(options: Bundle? = null) {
    openActivity(T::class.java, options)
}

inline fun <reified T : Activity> Activity.openActivity(requestCode: Int = 0, options: Bundle? = null) {
    openActivity(T::class.java, requestCode, options)
}

inline fun <reified T : Activity> Fragment.openActivity(requestCode: Int = 0, options: Bundle? = null) {
    openActivity(T::class.java, requestCode, options)
}
