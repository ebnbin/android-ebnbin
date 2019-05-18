package com.ebnbin.eb.sharedpreferences

open class Sp<T>(
    val key: String,
    val getDefaultValue: () -> T,
    val getSharedPreferencesNamePostfix: () -> String = { "" }
) {
    constructor(key: String, defaultValue: T) : this(key, { defaultValue })

    var value: T by SharedPreferencesProperty({ key }, getDefaultValue,
        { SharedPreferencesHelper.get(getSharedPreferencesNamePostfix()) })
}
