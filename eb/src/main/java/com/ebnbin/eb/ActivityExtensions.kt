package com.ebnbin.eb

import android.app.Activity

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
