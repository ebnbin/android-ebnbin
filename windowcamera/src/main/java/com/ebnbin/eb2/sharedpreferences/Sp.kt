package com.ebnbin.eb2.sharedpreferences

import android.content.SharedPreferences
import androidx.annotation.StringRes
import com.ebnbin.eb.app2.EBApp
import com.ebnbin.eb.sharedpreferences.get
import com.ebnbin.eb.sharedpreferences.getSharedPreferences
import com.ebnbin.eb.sharedpreferences.getSharedPreferencesName
import com.ebnbin.eb.sharedpreferences.put

open class Sp<T>(
    val getKey: () -> String?,
    val getDefaultValue: () -> T,
    val getSharedPreferencesName: () -> String? = { EBApp.instance.getSharedPreferencesName() },
    private val onChanged: ((oldValue: T?, newValue: T) -> Boolean)? = null
) {
    constructor(key: String, defaultValue: T) : this({ key }, { defaultValue })

    constructor(@StringRes keyId: Int, defaultValue: T) : this(EBApp.instance.resources.getString(keyId), defaultValue)

    var value: T
        get() {
            val defaultValue = getDefaultValue()
            val sharedPreferences = getSharedPreferences() ?: return defaultValue
            val key = getKey() ?: return defaultValue
            return sharedPreferences.get(key, defaultValue)
        }
        set(value) {
            val sharedPreferences = getSharedPreferences() ?: return
            val key = getKey() ?: return
            val defaultValue = getDefaultValue()
            val onChanged = onChanged
            if (onChanged != null) {
                val oldValue = if (sharedPreferences.contains(key)) sharedPreferences.get(key, defaultValue) else null
                if (onChanged(oldValue, value)) return
            }
            sharedPreferences.put(key, value)
        }

    fun getSharedPreferences(): SharedPreferences? {
        return getSharedPreferencesName()?.let { EBApp.instance.getSharedPreferences(it, false) }
    }

    fun requireSharedPreferencesName(): String {
        return getSharedPreferencesName() ?: throw RuntimeException()
    }

    fun requireSharedPreferences(): SharedPreferences? {
        return getSharedPreferences() ?: throw RuntimeException()
    }

    fun requireKey(): String {
        return getKey() ?: throw RuntimeException()
    }

    fun setDefaultValue(value: T): Boolean {
        val sharedPreferences = getSharedPreferences() ?: return false
        val key = getKey() ?: return false
        if (sharedPreferences.contains(key)) return false
        this.value = value
        return true
    }
}
