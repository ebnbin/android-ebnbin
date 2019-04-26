package com.ebnbin.eb.sharedpreferences.sp

import android.content.SharedPreferences
import com.ebnbin.eb.sharedpreferences.SharedPreferencesDelegate
import com.ebnbin.eb.sharedpreferences.SharedPreferencesHelper

open class Sp<T>(
    val key: String,
    val getDefaultValue: () -> T,
    getSharedPreferences: () -> SharedPreferences = { SharedPreferencesHelper.getSharedPreferences() }
) {
    var value: T by SharedPreferencesDelegate(key, getDefaultValue, getSharedPreferences)
}
