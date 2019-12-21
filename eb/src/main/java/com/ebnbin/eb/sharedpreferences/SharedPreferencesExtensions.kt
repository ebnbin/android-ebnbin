package com.ebnbin.eb.sharedpreferences

import android.content.SharedPreferences
import androidx.core.content.edit

fun <T> SharedPreferences.get(key: String, defaultValue: T): T {
    @Suppress("UNCHECKED_CAST")
    return when (defaultValue) {
        is String? -> getString(key, defaultValue) as T
        is Set<*>? -> getStringSet(key, setToStringSet(defaultValue)) as T
        is Int -> getInt(key, defaultValue) as T
        is Long -> getLong(key, defaultValue) as T
        is Float -> getFloat(key, defaultValue) as T
        is Boolean -> getBoolean(key, defaultValue) as T
        else -> throw IllegalArgumentException()
    }
}

fun <T> SharedPreferences.put(key: String, value: T) {
    edit {
        when (value) {
            is String? -> putString(key, value)
            is Set<*>? -> putStringSet(key, setToStringSet(value))
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            is Boolean -> putBoolean(key, value)
            else -> throw IllegalArgumentException()
        }
    }
}

/**
 * `Set<*>?` 转换 `Set<String?>?`. 如果失败则抛出异常.
 */
private fun setToStringSet(set: Set<*>?): Set<String?>? {
    if (set == null) return null
    val stringSet = LinkedHashSet<String?>()
    set.forEach {
        if (it !is String?) throw IllegalArgumentException()
        stringSet.add(it)
    }
    return stringSet
}
