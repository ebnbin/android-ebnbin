package com.ebnbin.eb

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * @param nameAppend 如果为 false 返回 [name], 否则返回 ${applicationId}_preferences[name].
 */
fun Context.getSharedPreferencesName(name: String = "", nameAppend: Boolean = true): String =
    if (nameAppend) "${applicationId}_preferences$name" else name

/**
 * 返回 name 为 [getSharedPreferencesName], mode 为 [Context.MODE_PRIVATE] 的 [SharedPreferences].
 */
fun Context.getSharedPreferences(name: String = "", nameAppend: Boolean = true): SharedPreferences =
    getSharedPreferences(getSharedPreferencesName(name, nameAppend), Context.MODE_PRIVATE)

//*********************************************************************************************************************

/**
 * @param defaultValue 不能为 null.
 */
@Suppress("UNCHECKED_CAST")
fun <T> SharedPreferences.get(key: String, defaultValue: T): T =
    when {
        defaultValue is String -> getString(key, defaultValue) as T
        defaultValue is Set<*> && defaultValue.all { it is String? } ->
            getStringSet(key, defaultValue.map { it as String? }.toSet()) as T
        defaultValue is Int -> getInt(key, defaultValue) as T
        defaultValue is Long -> getLong(key, defaultValue) as T
        defaultValue is Float -> getFloat(key, defaultValue) as T
        defaultValue is Boolean -> getBoolean(key, defaultValue) as T
        else -> throw UnsupportedOperationException("不支持的类型.")
    }

/**
 * @param value 不能为 null.
 */
fun <T> SharedPreferences.put(key: String, value: T) =
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
