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
) : SharedPreferences.OnSharedPreferenceChangeListener {
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

    //*****************************************************************************************************************

    private val onChangeds: ArrayList<(value: T) -> Unit> = ArrayList()

    fun addOnChanged(onChanged: (value: T) -> Unit) {
        if (onChangeds.isEmpty()) {
            getSharedPreferences().registerOnSharedPreferenceChangeListener(this)
        }
        onChangeds.add(onChanged)
    }

    fun removeOnChanged(onChanged: (value: T) -> Unit) {
        onChangeds.remove(onChanged)
        if (onChangeds.isEmpty()) {
            getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this)
        }
    }

    fun addOnChanged(lifecycle: Lifecycle, onChanged: (value: T) -> Unit) {
        lifecycle.addObserver(object : LifecycleObserver {
            @Suppress("unused")
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                removeOnChanged(onChanged)
            }
        })
        addOnChanged(onChanged)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (sharedPreferences !== getSharedPreferences() || key != this.key) return
        onChangeds.forEach {
            it(get())
        }
    }
}
