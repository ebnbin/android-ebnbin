package com.ebnbin.eb.sharedpreferences.sp

import com.ebnbin.eb.sharedpreferences.SharedPreferencesHelper

open class EBDebugSp<T>(key: String, getDefaultValue: () -> T) :
    Sp<T>(key, getDefaultValue, { SharedPreferencesHelper.getSharedPreferences("_eb_debug") }, null) {
    constructor(key: String, defaultValue: T) : this(key, { defaultValue })
}
