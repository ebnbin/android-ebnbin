package com.ebnbin.eb.sharedpreferences.delegate

import com.ebnbin.eb.sharedpreferences.defaultSharedPreferencesName
import com.ebnbin.eb.sharedpreferences.getSharedPreferences

/**
 * `_eb_debug` 偏好代理.
 */
internal class EBDebugSharedPreferencesDelegate<T>(
    key: String,
    defValue: T,
    onSetValue: ((oldValue: T, newValue: T) -> Boolean)? = null
) : SharedPreferencesDelegate<T>(
    key,
    defValue,
    { getSharedPreferences("${defaultSharedPreferencesName}_eb_debug") },
    onSetValue
)
