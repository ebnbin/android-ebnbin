package com.ebnbin.eb.sharedpreferences.sp

import android.content.SharedPreferences
import com.ebnbin.eb.sharedpreferences.SharedPreferencesProperty
import com.ebnbin.eb.sharedpreferences.SharedPreferencesHelper

open class Sp<T>(
    val key: String,
    val getDefaultValue: () -> T,
    val getSharedPreferences: () -> SharedPreferences = { SharedPreferencesHelper.get() }
) {
    var value: T by SharedPreferencesProperty(key, getDefaultValue, getSharedPreferences)
}
