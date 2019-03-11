package com.ebnbin.eb.sharedpreferences.delegate

import android.content.SharedPreferences
import com.ebnbin.eb.sharedpreferences.get
import com.ebnbin.eb.sharedpreferences.put
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 偏好代理.
 */
open class SharedPreferencesDelegate<T>(
    private val key: String,
    private val defValue: T,
    /**
     * 获取偏好.
     */
    private val getSharedPreferences: () -> SharedPreferences = {
        com.ebnbin.eb.sharedpreferences.getSharedPreferences()
    },
    /**
     * 如果不为空则读取旧的值用于拦截偏好设置. 返回 true 则拦截偏好设置.
     */
    private val onSetValue: ((oldValue: T, newValue: T) -> Boolean)? = null
) : ReadWriteProperty<Any?, T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return getSharedPreferences().get(key, defValue)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        val sharedPreferences = getSharedPreferences()
        if (onSetValue?.invoke(sharedPreferences.get(key, defValue), value) != true) {
            sharedPreferences.put(key, value)
        }
    }
}
