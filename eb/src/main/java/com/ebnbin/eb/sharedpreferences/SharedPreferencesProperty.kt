package com.ebnbin.eb.sharedpreferences

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 偏好属性代理.
 */
class SharedPreferencesProperty<T>(
    private val getKey: () -> String?,
    private val getDefaultValue: () -> T,
    private val getSharedPreferences: () -> SharedPreferences? = { SharedPreferencesHelper.get() },
    /**
     * 如果不为空则读取旧的值用于拦截偏好设置. 返回 true 则拦截偏好设置.
     */
    private val onSetValue: ((oldValue: T, newValue: T) -> Boolean)? = null
) : ReadWriteProperty<Any?, T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val key = getKey()
        val defaultValue = getDefaultValue()
        val sharedPreferences = getSharedPreferences()
        return if (key == null || sharedPreferences == null) {
            defaultValue
        } else {
            sharedPreferences.get(key, defaultValue)
        }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        val key = getKey()
        val defaultValue = getDefaultValue()
        val sharedPreferences = getSharedPreferences()
        if (key == null || sharedPreferences == null) return
        if (onSetValue?.invoke(sharedPreferences.get(key, defaultValue), value) != true) {
            sharedPreferences.put(key, value)
        }
    }
}
