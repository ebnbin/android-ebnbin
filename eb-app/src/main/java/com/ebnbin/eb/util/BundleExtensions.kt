package com.ebnbin.eb.util

import android.os.Bundle

inline fun <reified T> Bundle.getValue(key: String): T? {
    return get(key) as T?
}

/**
 * 如果 key 不存在则抛出异常.
 */
inline fun <reified T> Bundle.requireValue(key: String): T {
    return get(key) as T
}

/**
 * 如果 key 不存在则返回默认值.
 */
inline fun <reified T> Bundle.getValueOrDefault(key: String, defaultValue: T): T {
    return if (containsKey(key)) requireValue(key) else defaultValue
}
