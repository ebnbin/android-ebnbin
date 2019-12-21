package com.ebnbin.eb.sharedpreferences

import android.content.Context
import android.content.SharedPreferences

open class Sp<T>(
    private val context: Context,
    val key: String,
    val defaultValue: T,
    val sharedPreferencesName: String = context.getSharedPreferencesName()
) {
    var value: T
        get() = internalGet()
        set(value) {
            if (internalGet() == value) return
            internalPut(value)
        }

    /**
     * 设置默认值.
     *
     * @return 如果已存在 key 则返回 false.
     */
    fun setDefaultValue(defaultValue: T): Boolean {
        if (internalContains()) return false
        internalPut(defaultValue)
        return true
    }

    fun getSharedPreferences(): SharedPreferences {
        return context.getSharedPreferences(sharedPreferencesName, false)
    }

    private fun internalContains(): Boolean {
        return getSharedPreferences().contains(key)
    }

    private fun internalGet(): T {
        return getSharedPreferences().get(key, defaultValue)
    }

    private fun internalPut(value: T) {
        getSharedPreferences().put(key, value)
    }
}
