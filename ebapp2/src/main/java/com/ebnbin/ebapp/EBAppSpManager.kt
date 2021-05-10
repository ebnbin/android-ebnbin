package com.ebnbin.ebapp

import androidx.appcompat.app.AppCompatDelegate
import com.ebnbin.eb.EBApplication
import com.ebnbin.eb.sharedpreferences.Sp
import com.ebnbin.eb.sharedpreferences.getSharedPreferencesName

object EBAppSpManager {
    internal val name: String
        get() = EBApplication.instance.getSharedPreferencesName("_ebapp")

    val night_mode: Sp<String> = Sp(
        { EBApplication.instance },
        "night_mode",
        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM.toString(),
        { name }
    ).also {
        it.addOnChanged { value ->
            value.toIntOrNull()?.let { mode ->
                AppCompatDelegate.setDefaultNightMode(mode)
            }
        }
    }
}
