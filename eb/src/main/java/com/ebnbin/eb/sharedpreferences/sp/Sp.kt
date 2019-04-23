package com.ebnbin.eb.sharedpreferences.sp

import android.content.SharedPreferences
import com.ebnbin.eb.sharedpreferences.SharedPreferencesDelegate
import com.ebnbin.eb.sharedpreferences.SharedPreferencesHelper

open class Sp<T>(
    val key: String,
    val getDefaultValue: () -> T,
    getSharedPreferences: () -> SharedPreferences,
    onSetValue: ((oldValue: T, newValue: T) -> Boolean)?
) {
    constructor(key: String, getDefaultValue: () -> T) :
            this(key, getDefaultValue, { SharedPreferencesHelper.getSharedPreferences() }, null)

    constructor(key: String, defaultValue: T) : this(key, { defaultValue })

    var value: T by SharedPreferencesDelegate(key, getDefaultValue, getSharedPreferences, onSetValue)
}
