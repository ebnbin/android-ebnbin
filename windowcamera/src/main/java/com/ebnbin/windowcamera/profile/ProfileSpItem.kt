package com.ebnbin.windowcamera.profile

import com.ebnbin.eb.sharedpreferences.SpHelper
import com.ebnbin.eb.sharedpreferences.SpItem

open class ProfileSpItem<T>(key: String, getDefaultValue: () -> T) : SpItem<T>(
    key,
    getDefaultValue,
    { SpHelper.getSharedPreferences(ProfileHelper.sharedPreferencesName) },
    null
) {
    constructor(key: String, defaultValue: T) : this(key, { defaultValue })
}
