package com.ebnbin.eb2.sharedpreferences

import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * 读偏好.
 */
fun <T> SharedPreferences.get(key: String, defValue: T): T {
    @Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
    return when (defValue) {
        is String? -> getString(key, defValue)
        is Set<*>? -> getStringSet(key, setToStringSet(defValue))
        is Int -> getInt(key, defValue)
        is Long -> getLong(key, defValue)
        is Float -> getFloat(key, defValue)
        is Boolean -> getBoolean(key, defValue)
        else -> throw RuntimeException()
    } as T
}

/**
 * 写偏好.
 */
fun <T> SharedPreferences.put(key: String, value: T) {
    edit {
        when (value) {
            is String? -> putString(key, value)
            is Set<*>? -> putStringSet(key, setToStringSet(value))
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            is Boolean -> putBoolean(key, value)
            else -> throw RuntimeException()
        }
    }
}

/**
 * `Set<*>?` 转换 `Set<String?>?`.
 */
private fun setToStringSet(set: Set<*>?): Set<String?>? {
    if (set == null) return null
    val stringSet = LinkedHashSet<String?>()
    set.forEach {
        if (it !is String?) throw RuntimeException()
        stringSet.add(it)
    }
    return stringSet
}
