package com.ebnbin.eb.sharedpreferences

import android.content.SharedPreferences

open class Sp<T>(
    val key: String,
    val getDefaultValue: () -> T,
    val getSharedPreferences: () -> SharedPreferences = { SharedPreferencesHelper.get() }
) {
    var value: T by SharedPreferencesProperty(key, getDefaultValue, getSharedPreferences)
}
