package com.ebnbin.windowcamera.profile

import com.ebnbin.eb.sharedpreferences.delegate.SharedPreferencesDelegate
import com.ebnbin.eb.sharedpreferences.getSharedPreferences

class ProfileSharedPreferencesDelegate<T>(
    key: String,
    defValue: T,
    onSetValue: ((oldValue: T, newValue: T) -> Boolean)? = null
) : SharedPreferencesDelegate<T>(
    key,
    defValue,
    { getSharedPreferences(ProfileHelper.sharedPreferencesName) },
    onSetValue
)
