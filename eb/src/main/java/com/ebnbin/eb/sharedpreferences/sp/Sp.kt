package com.ebnbin.eb.sharedpreferences.sp

import android.content.SharedPreferences
import com.ebnbin.eb.sharedpreferences.SharedPreferencesDelegate
import com.ebnbin.eb.sharedpreferences.SharedPreferencesHelper
import com.ebnbin.eb.util.ebApp

open class Sp<T>(
    val key: String,
    val getDefaultValue: () -> T,
    getSharedPreferences: () -> SharedPreferences,
    onSetValue: ((oldValue: T, newValue: T) -> Boolean)?
) {
    constructor(key: String, getDefaultValue: () -> T) :
            this(key, getDefaultValue, { SharedPreferencesHelper.getSharedPreferences() }, null)

    constructor(key: String, defaultValue: T) : this(key, { defaultValue })

    constructor(keyId: Int, getDefaultValue: () -> T) : this(ebApp.getString(keyId), getDefaultValue)

    constructor(keyId: Int, defaultValue: T) : this(ebApp.getString(keyId), defaultValue)

    var value: T by SharedPreferencesDelegate(key, getDefaultValue, getSharedPreferences, onSetValue)
}
