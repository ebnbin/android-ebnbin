package com.ebnbin.eb.sharedpreferences

import android.content.SharedPreferences

open class SpItem<T>(
    val key: String,
    val getDefaultValue: () -> T,
    getSharedPreferences: () -> SharedPreferences,
    onSetValue: ((oldValue: T, newValue: T) -> Boolean)?
) {
    constructor(key: String, getDefaultValue: () -> T) :
            this(key, getDefaultValue, { SpHelper.getSharedPreferences() }, null)

    constructor(key: String, defaultValue: T) : this(key, { defaultValue })

    var value: T by SharedPreferencesDelegate(key, getDefaultValue, getSharedPreferences, onSetValue)
}
