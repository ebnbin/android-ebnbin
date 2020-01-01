package com.ebnbin.eb.sharedpreferences

import android.content.Context
import android.content.SharedPreferences

open class Sp<T>(
    private val getContext: () -> Context,
    val key: String,
    val defaultValue: T,
    val getName: () -> String = { getContext().getSharedPreferencesName() }
) {
    var value: T
        get() = get()
        set(value) {
            if (get() == value) return
            put(value)
        }

    fun getSharedPreferences(): SharedPreferences {
        return getContext().getSharedPreferences(getName(), false)
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
