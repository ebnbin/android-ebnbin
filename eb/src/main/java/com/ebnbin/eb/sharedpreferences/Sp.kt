package com.ebnbin.eb.sharedpreferences

import android.content.Context
import android.content.SharedPreferences

open class Sp<T>(
    context: Context,
    val key: String,
    val defaultValue: T,
    val sharedPreferencesName: String = context.getSharedPreferencesName()
) {
    private val context: Context = context.applicationContext

    var value: T
        get() = get()
        set(value) {
            if (get() == value) return
            put(value)
        }

    fun getSharedPreferences(): SharedPreferences {
        return context.getSharedPreferences(sharedPreferencesName, false)
    }

    fun contains(): Boolean {
        return getSharedPreferences().contains(key)
    }

    private fun get(): T {
        return getSharedPreferences().get(key, defaultValue)
    }

    private fun put(value: T) {
        getSharedPreferences().put(key, value)
    }

    fun remove() {
        getSharedPreferences().remove(key)
    }
}
