package com.ebnbin.eb.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import com.ebnbin.eb.applicationId

fun Context.getSharedPreferencesName(name: String = "", nameAppend: Boolean = true): String {
    return if (nameAppend) "${applicationId}_preferences${name}" else name
}

fun Context.getSharedPreferences(name: String = "", nameAppend: Boolean = true): SharedPreferences {
    return getSharedPreferences(getSharedPreferencesName(name, nameAppend), Context.MODE_PRIVATE)
}
