package com.ebnbin.eb.sharedpreferences

open class EBDebugSpItem<T>(key: String, getDefaultValue: () -> T) : SpItem<T>(
    key,
    getDefaultValue,
    { SpHelper.getSharedPreferences("${SpHelper.defaultSharedPreferencesName}_eb_debug") },
    null
) {
    constructor(key: String, defaultValue: T) : this(key, { defaultValue })
}
