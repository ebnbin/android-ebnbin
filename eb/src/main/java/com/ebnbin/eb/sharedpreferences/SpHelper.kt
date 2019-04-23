package com.ebnbin.eb.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import com.ebnbin.eb.util.ebApp

object SpHelper {
    /**
     * 默认偏好文件名.
     */
    val defaultSharedPreferencesName: String = "${ebApp.packageName}_preferences"

    /**
     * 默认偏好模式.
     */
    val defaultSharedPreferencesMode: Int = Context.MODE_PRIVATE

    fun getSharedPreferences(
        name: String = defaultSharedPreferencesName,
        mode: Int = defaultSharedPreferencesMode
    ): SharedPreferences {
        return ebApp.getSharedPreferences(name, mode)
    }
}
