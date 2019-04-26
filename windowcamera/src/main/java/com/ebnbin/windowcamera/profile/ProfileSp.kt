package com.ebnbin.windowcamera.profile

import com.ebnbin.eb.sharedpreferences.SharedPreferencesHelper
import com.ebnbin.eb.sharedpreferences.sp.Sp
import com.ebnbin.eb.util.res

open class ProfileSp<T>(key: String, getDefaultValue: () -> T) : Sp<T>(
    key,
    getDefaultValue,
    { SharedPreferencesHelper.getSharedPreferences(ProfileHelper.sharedPreferencesNamePostfix) }
) {
    constructor(keyId: Int, getDefaultValue: () -> T) : this(res.getString(keyId), getDefaultValue)
}
