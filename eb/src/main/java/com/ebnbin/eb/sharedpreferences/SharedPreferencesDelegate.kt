package com.ebnbin.eb.sharedpreferences

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 偏好代理.
 */
internal class SharedPreferencesDelegate<T>(
    private val key: String,
    private val getDefaultValue: () -> T,
    /**
     * 获取偏好.
     */
    private val getSharedPreferences: () -> SharedPreferences,
    /**
     * 如果不为空则读取旧的值用于拦截偏好设置. 返回 true 则拦截偏好设置.
     */
    private val onSetValue: ((oldValue: T, newValue: T) -> Boolean)?
) : ReadWriteProperty<Any?, T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return getSharedPreferences().get(key, getDefaultValue())
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        val sharedPreferences = getSharedPreferences()
        if (onSetValue?.invoke(sharedPreferences.get(key, getDefaultValue()), value) != true) {
            sharedPreferences.put(key, value)
        }
    }
}
