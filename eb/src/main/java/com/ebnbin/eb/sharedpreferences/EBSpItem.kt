package com.ebnbin.eb.sharedpreferences

open class EBSpItem<T>(key: String, getDefaultValue: () -> T) : SpItem<T>(
    key,
    getDefaultValue,
    { SpHelper.getSharedPreferences("${SpHelper.defaultSharedPreferencesName}_eb") },
    null
) {
    constructor(key: String, defaultValue: T) : this(key, { defaultValue })
}
