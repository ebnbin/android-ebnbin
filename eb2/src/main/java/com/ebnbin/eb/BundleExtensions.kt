package com.ebnbin.eb

import android.os.Bundle

/**
 * 返回 value 或 null.
 */
inline fun <reified T> Bundle.getValue(key: String): T? {
    return get(key) as T?
}

/**
 * 返回 value 或抛出 [NullPointerException].
 */
inline fun <reified T> Bundle.requireValue(key: String): T {
    return get(key) as T
}

/**
 * 如果 [key] 不存在则返回 [defaultValue], 否则返回 value 或抛出 [NullPointerException].
 */
inline fun <reified T> Bundle.getValueOrDefault(key: String, defaultValue: T): T {
    return if (containsKey(key)) requireValue(key) else defaultValue
}
