package com.ebnbin.eb2.util

import android.Manifest
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatDelegate
import com.ebnbin.eb.EBApplication
import com.ebnbin.eb.requireSystemService
import com.ebnbin.eb.sdk26O
import com.ebnbin.eb2.sharedpreferences.EBSpManager

object AppHelper {
    @RequiresPermission(Manifest.permission.VIBRATE)
    fun vibrate(milliseconds: Long) {
        if (sdk26O()) {
            EBApplication.instance.requireSystemService<Vibrator>().vibrate(
                VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            EBApplication.instance.requireSystemService<Vibrator>().vibrate(milliseconds)
        }
    }

    fun setNightMode(nightMode: Int, restartMainActivity: Boolean = false) {
        if (nightMode == EBSpManager.night_mode.value) return
        EBSpManager.night_mode.value = nightMode
        AppCompatDelegate.setDefaultNightMode(nightMode)
        if (restartMainActivity) {
            IntentHelper.restartApp()
        }
    }
}
