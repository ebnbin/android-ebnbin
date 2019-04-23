package com.ebnbin.eb.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import com.ebnbin.eb.util.ebApp

object SharedPreferencesHelper {
    fun getSharedPreferencesName(namePostfix: String = ""): String {
        return "${ebApp.packageName}_preferences$namePostfix"
    }

    /**
     * "_eb", "_profile".
     */
    fun getSharedPreferences(namePostfix: String = ""): SharedPreferences {
        return ebApp.getSharedPreferences(getSharedPreferencesName(namePostfix), Context.MODE_PRIVATE)
    }
}
