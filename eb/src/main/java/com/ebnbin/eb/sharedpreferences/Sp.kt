package com.ebnbin.eb.sharedpreferences

import android.content.SharedPreferences

open class Sp<T>(
    val key: String,
    val getDefaultValue: () -> T,
    val getSharedPreferences: () -> SharedPreferences = { SharedPreferencesHelper.get() }
) {
    constructor(key: String, defaultValue: T) : this(key, { defaultValue })

    var value: T by SharedPreferencesProperty(key, getDefaultValue, getSharedPreferences)
}
