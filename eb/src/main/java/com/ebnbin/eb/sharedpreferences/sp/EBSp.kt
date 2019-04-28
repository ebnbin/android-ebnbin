package com.ebnbin.eb.sharedpreferences.sp

import com.ebnbin.eb.sharedpreferences.SharedPreferencesHelper

open class EBSp<T>(key: String, getDefaultValue: () -> T) : Sp<T>(
    key,
    getDefaultValue,
    { SharedPreferencesHelper.get("_eb") }
) {
    constructor(key: String, defaultValue: T) : this(key, { defaultValue })
}
