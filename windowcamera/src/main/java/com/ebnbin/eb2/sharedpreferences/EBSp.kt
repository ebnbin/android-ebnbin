package com.ebnbin.eb2.sharedpreferences

open class EBSp<T>(
    getKey: () -> String?,
    getDefaultValue: () -> T,
    onChanged: ((oldValue: T?, newValue: T) -> Boolean)? = null
) : Sp<T>(
    getKey,
    getDefaultValue,
    { SharedPreferencesHelper.getName("_eb") },
    onChanged
) {
    constructor(key: String, defaultValue: T) : this({ key }, { defaultValue })
}
