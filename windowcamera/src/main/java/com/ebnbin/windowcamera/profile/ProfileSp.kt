package com.ebnbin.windowcamera.profile

import com.ebnbin.eb.sharedpreferences.SharedPreferencesHelper
import com.ebnbin.eb.sharedpreferences.sp.Sp

open class ProfileSp<T>(key: String, getDefaultValue: () -> T) : Sp<T>(
    key,
    getDefaultValue,
    { SharedPreferencesHelper.getSharedPreferences(ProfileHelper.sharedPreferencesNamePostfix) },
    null
) {
    constructor(key: String, defaultValue: T) : this(key, { defaultValue })
}
