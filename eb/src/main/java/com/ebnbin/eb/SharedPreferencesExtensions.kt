package com.ebnbin.eb

import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * @param defaultValue 不能为 null.
 */
fun <T> SharedPreferences.get(key: String, defaultValue: T): T {
    @Suppress("UNCHECKED_CAST")
    return when {
        defaultValue is String -> getString(key, defaultValue) as T
        defaultValue is Set<*> && defaultValue.all { it is String? } ->
            getStringSet(key, defaultValue.map { it as String? }.toSet()) as T
        defaultValue is Int -> getInt(key, defaultValue) as T
        defaultValue is Long -> getLong(key, defaultValue) as T
        defaultValue is Float -> getFloat(key, defaultValue) as T
        defaultValue is Boolean -> getBoolean(key, defaultValue) as T
        else -> throw UnsupportedOperationException("不支持的类型.")
    }
}

/**
 * @param value 不能为 null.
 */
fun <T> SharedPreferences.put(key: String, value: T) {
    edit {
        when {
            value is String -> putString(key, value)
            value is Set<*> && value.all { it is String? } -> putStringSet(key, value.map { it as String? }.toSet())
            value is Int -> putInt(key, value)
            value is Long -> putLong(key, value)
            value is Float -> putFloat(key, value)
            value is Boolean -> putBoolean(key, value)
            else -> throw UnsupportedOperationException("不支持的类型.")
        }
    }
}
