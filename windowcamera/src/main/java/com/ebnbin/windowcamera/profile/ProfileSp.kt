package com.ebnbin.windowcamera.profile

import androidx.annotation.StringRes
import com.ebnbin.eb.sharedpreferences.SharedPreferencesHelper
import com.ebnbin.eb.sharedpreferences.Sp
import com.ebnbin.eb.util.res

open class ProfileSp<T>(key: String, getDefaultValue: () -> T) : Sp<T>(
    key,
    getDefaultValue,
    { SharedPreferencesHelper.get(ProfileHelper.SHARED_PREFERENCES_NAME_POSTFIX) }
) {
    constructor(key: String, defaultValue: T) : this(key, { defaultValue })

    constructor(@StringRes keyId: Int, getDefaultValue: () -> T) : this(res.getString(keyId), getDefaultValue)

    constructor(@StringRes keyId: Int, defaultValue: T) : this(res.getString(keyId), { defaultValue })
}
