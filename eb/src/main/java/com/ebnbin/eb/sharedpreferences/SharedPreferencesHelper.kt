package com.ebnbin.eb.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import com.ebnbin.eb.util.BuildHelper
import com.ebnbin.eb.util.ebApp

object SharedPreferencesHelper {
    fun getDefaultName(): String {
        return "${BuildHelper.applicationId}_preferences"
    }

    fun getSharedPreferences(name: String): SharedPreferences {
        return ebApp.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    fun getName(namePostfix: String = ""): String {
        return "${getDefaultName()}$namePostfix"
    }

    fun get(namePostfix: String = ""): SharedPreferences {
        return getSharedPreferences(getName(namePostfix))
    }
}
