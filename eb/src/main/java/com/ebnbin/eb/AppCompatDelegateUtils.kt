package com.ebnbin.eb

import androidx.appcompat.app.AppCompatDelegate

fun getNightModeToString(nightMode: Int): String {
    return when (nightMode) {
        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> "MODE_NIGHT_FOLLOW_SYSTEM"
        AppCompatDelegate.MODE_NIGHT_NO -> "MODE_NIGHT_NO"
        AppCompatDelegate.MODE_NIGHT_YES -> "MODE_NIGHT_YES"
        AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY -> "MODE_NIGHT_AUTO_BATTERY"
        AppCompatDelegate.MODE_NIGHT_UNSPECIFIED -> "MODE_NIGHT_UNSPECIFIED"
        else -> nightMode.toString()
    }
}
