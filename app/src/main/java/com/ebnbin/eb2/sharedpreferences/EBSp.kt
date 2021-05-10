package com.ebnbin.eb2.sharedpreferences

import com.ebnbin.eb.EBApplication
import com.ebnbin.eb.sharedpreferences.getSharedPreferencesName

open class EBSp<T>(
    getKey: () -> String?,
    getDefaultValue: () -> T,
    onChanged: ((oldValue: T?, newValue: T) -> Boolean)? = null
) : Sp<T>(
    getKey,
    getDefaultValue,
    { EBApplication.instance.getSharedPreferencesName("_eb") },
    onChanged
) {
    constructor(key: String, defaultValue: T) : this({ key }, { defaultValue })
}
