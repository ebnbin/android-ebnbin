package com.ebnbin.eb.util

import android.Manifest
import android.app.Service
import android.content.ClipData
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatDelegate
import com.ebnbin.eb.sharedpreferences.EBSpManager

object AppHelper {
    fun isServiceRunning(serviceClass: Class<out Service>): Boolean {
        @Suppress("DEPRECATION")
        return SystemServices.activityManager.getRunningServices(Int.MAX_VALUE).any {
            it.service.className == serviceClass.name
        }
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    fun vibrate(milliseconds: Long) {
        if (BuildHelper.sdk26O()) {
            SystemServices.vibrator.vibrate(
                VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            SystemServices.vibrator.vibrate(milliseconds)
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

    fun toast(context: Context, any: Any?, long: Boolean = false) {
        val duration = if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        Toast.makeText(context, ResHelper.getString(any), duration).show()
    }

    fun clip(text: CharSequence, label: CharSequence = BuildHelper.applicationId) {
        SystemServices.clipboardManager.primaryClip = ClipData.newPlainText(label, text)
    }

    val mainHandler: Handler = Handler(Looper.getMainLooper())
}
