package com.ebnbin.eb.sharedpreferences.sp

import com.ebnbin.eb.sharedpreferences.SharedPreferencesHelper
import com.ebnbin.eb.util.ebApp

open class EBDebugSp<T>(key: String, getDefaultValue: () -> T) :
    Sp<T>(key, getDefaultValue, { SharedPreferencesHelper.getSharedPreferences("_eb_debug") }, null) {
    constructor(key: String, defaultValue: T) : this(key, { defaultValue })

    constructor(keyId: Int, getDefaultValue: () -> T) : this(ebApp.getString(keyId), getDefaultValue)

    constructor(keyId: Int, defaultValue: T) : this(ebApp.getString(keyId), defaultValue)
}
