package com.ebnbin.windowcamera.profile

import com.ebnbin.eb.sharedpreferences.SharedPreferencesHelper
import com.ebnbin.eb.sharedpreferences.sp.Sp
import com.ebnbin.eb.util.ebApp

open class ProfileSp<T>(key: String, getDefaultValue: () -> T) : Sp<T>(
    key,
    getDefaultValue,
    { SharedPreferencesHelper.getSharedPreferences(ProfileHelper.sharedPreferencesNamePostfix) },
    null
) {
    constructor(key: String, defaultValue: T) : this(key, { defaultValue })

    constructor(keyId: Int, getDefaultValue: () -> T) : this(ebApp.getString(keyId), getDefaultValue)

    constructor(keyId: Int, defaultValue: T) : this(ebApp.getString(keyId), defaultValue)
}
