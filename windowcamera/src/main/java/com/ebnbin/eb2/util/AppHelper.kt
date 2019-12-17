package com.ebnbin.eb2.util

import android.Manifest
import android.app.ActivityManager
import android.app.Service
import android.content.ClipData
import android.content.ClipboardManager
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatDelegate
import com.ebnbin.eb.EBApp
import com.ebnbin.eb.extension.requireSystemService
import com.ebnbin.eb2.sharedpreferences.EBSpManager

object AppHelper {
    fun isServiceRunning(serviceClass: Class<out Service>): Boolean {
        @Suppress("DEPRECATION")
        return EBApp.instance.requireSystemService<ActivityManager>().getRunningServices(Int.MAX_VALUE).any {
            it.service.className == serviceClass.name
        }
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    fun vibrate(milliseconds: Long) {
        if (BuildHelper.sdk26O()) {
            EBApp.instance.requireSystemService<Vibrator>().vibrate(
                VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            EBApp.instance.requireSystemService<Vibrator>().vibrate(milliseconds)
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

    fun copy(text: CharSequence, label: CharSequence = BuildHelper.applicationId) {
        EBApp.instance.requireSystemService<ClipboardManager>().setPrimaryClip(ClipData.newPlainText(label, text))
    }

    val mainHandler: Handler = Handler(Looper.getMainLooper())
}
