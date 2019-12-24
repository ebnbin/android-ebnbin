package com.ebnbin.eb

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * 返回 extra 是否存在.
 */
fun Activity.containsExtra(key: String): Boolean = intent?.extras?.containsKey(key) ?: false

/**
 * 返回 extra 或 null.
 */
inline fun <reified T> Activity.getExtra(key: String): T? = intent?.extras?.get(key) as T?

/**
 * 返回 extra 或抛出 [NullPointerException].
 */
inline fun <reified T> Activity.requireExtra(key: String): T = intent?.extras?.get(key) as T

/**
 * 如果 [key] 不存在则返回 [defaultValue], 否则返回 extra 或抛出 [NullPointerException].
 */
inline fun <reified T> Activity.getExtraOrDefault(key: String, defaultValue: T): T =
    if (containsExtra(key)) requireExtra(key) else defaultValue

//*********************************************************************************************************************

fun Context.openActivity(intent: Intent, options: Bundle? = null) =
    startActivity(intent, options)

fun Activity.openActivity(intent: Intent, requestCode: Int = 0, options: Bundle? = null) =
    startActivityForResult(intent, requestCode, options)

fun Fragment.openActivity(intent: Intent, requestCode: Int = 0, options: Bundle? = null) =
    startActivityForResult(intent, requestCode, options)
