package com.ebnbin.eb.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

open class Sp<T>(
    private val getContext: () -> Context,
    val key: String,
    val defaultValue: T,
    val getName: () -> String = { getContext().getSharedPreferencesName() }
) {
    var value: T
        get() = get()
        set(value) {
            val oldValue = get()
            if (oldValue == value) return
            val intercept = onChanges.map {
                it(oldValue, value)
            }.any { it }
            if (!intercept) put(value)
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

    //*****************************************************************************************************************

    private val onChanges: ArrayList<(oldValue: T, newValue: T) -> Boolean> = ArrayList()

    fun addOnChange(lifecycle: Lifecycle, onChange: (oldValue: T, newValue: T) -> Boolean) {
        lifecycle.addObserver(object : LifecycleObserver {
            @Suppress("unused")
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                onChanges.remove(onChange)
            }
        })
        onChanges.add(onChange)
    }
}
