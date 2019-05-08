package com.ebnbin.eb.util

import android.Manifest
import android.app.Service
import android.content.ClipData
import android.content.Context
import android.os.VibrationEffect
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatDelegate
import com.ebnbin.eb.R
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

    fun setNightMode(nightMode: Int, restartMainActivity: Boolean = true) {
        if (nightMode == EBSpManager.night_mode.value) return
        EBSpManager.night_mode.value = nightMode
        AppCompatDelegate.setDefaultNightMode(nightMode)
        if (restartMainActivity) {
            IntentHelper.restartApp()
        }
    }

    /**
     * @param any 如果为 Int 则取字符串资源.
     */
    fun toast(context: Context, any: Any?, long: Boolean = false) {
        val duration = if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        if (any is Int) {
            Toast.makeText(context, any, duration).show()
        } else {
            Toast.makeText(context, any.toString(), duration).show()
        }
    }

    fun clip(
        context: Context,
        text: CharSequence,
        toast: CharSequence = context.getString(R.string.eb_clip),
        label: CharSequence = BuildHelper.applicationId
    ) {
        SystemServices.clipboardManager.primaryClip = ClipData.newPlainText(label, text)
        toast(context, toast)
    }
}
