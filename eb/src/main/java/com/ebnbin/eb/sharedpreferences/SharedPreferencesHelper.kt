package com.ebnbin.eb.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import com.ebnbin.eb.util.ebApp

object SharedPreferencesHelper {
    /**
     * "_eb", "_profile".
     */
    fun getName(namePostfix: String = ""): String {
        return "${ebApp.packageName}_preferences$namePostfix"
    }

    /**
     * "_eb", "_profile".
     */
    fun get(namePostfix: String = ""): SharedPreferences {
        return ebApp.getSharedPreferences(getName(namePostfix), Context.MODE_PRIVATE)
    }
}
