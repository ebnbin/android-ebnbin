package com.ebnbin.eb.sharedpreferences.sp

import com.ebnbin.eb.sharedpreferences.SharedPreferencesHelper

open class EBDebugSp<T>(key: String, getDefaultValue: () -> T) : Sp<T>(
    key,
    getDefaultValue,
    { SharedPreferencesHelper.get("_eb_debug") }
) {
    constructor(key: String, defaultValue: T) : this(key, { defaultValue })
}
