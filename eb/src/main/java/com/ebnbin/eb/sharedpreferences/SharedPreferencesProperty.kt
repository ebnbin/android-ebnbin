package com.ebnbin.eb.sharedpreferences

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 偏好属性代理.
 */
internal class SharedPreferencesProperty<T>(
    private val key: String,
    private val getDefaultValue: () -> T,
    private val getSharedPreferences: () -> SharedPreferences
) : ReadWriteProperty<Any?, T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return getSharedPreferences().get(key, getDefaultValue())
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        getSharedPreferences().put(key, value)
    }
}
