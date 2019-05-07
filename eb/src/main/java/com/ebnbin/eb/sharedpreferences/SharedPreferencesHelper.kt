package com.ebnbin.eb.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import com.ebnbin.eb.util.BuildHelper
import com.ebnbin.eb.util.ebApp

object SharedPreferencesHelper {
    /**
     * "_eb", "_profile".
     */
    fun getName(namePostfix: String = ""): String {
        return "${BuildHelper.applicationId}_preferences$namePostfix"
    }

    /**
     * "_eb", "_profile".
     */
    fun get(namePostfix: String = ""): SharedPreferences {
        return ebApp.getSharedPreferences(getName(namePostfix), Context.MODE_PRIVATE)
    }

    fun register(listener: SharedPreferences.OnSharedPreferenceChangeListener, namePostfix: String = "") {
        get(namePostfix).registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregister(listener: SharedPreferences.OnSharedPreferenceChangeListener, namePostfix: String = "") {
        get(namePostfix).unregisterOnSharedPreferenceChangeListener(listener)
    }
}
