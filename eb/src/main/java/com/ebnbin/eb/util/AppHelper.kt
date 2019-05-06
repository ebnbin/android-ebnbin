package com.ebnbin.eb.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Service
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.os.VibrationEffect
import android.provider.Settings
import android.util.Base64
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatDelegate
import com.ebnbin.eb.R
import com.ebnbin.eb.sharedpreferences.EBSpManager
import java.security.MessageDigest

object AppHelper {
    /**
     * 关闭所以 Activity 并重启 MainActivity.
     *
     * @param finish 如果为 true 则关闭 MainActivity, 相当于关闭应用所有 Activity.
     */
    fun restartMainActivity(finish: Boolean = false) {
        val intent = ebApp.packageManager.getLaunchIntentForPackage(ebApp.packageName) ?: return
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.putExtra(Consts.FINISH, finish)
        ebApp.startActivity(intent)
    }

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
        if (nightMode == EBSpManager.eb.night_mode.value) return
        EBSpManager.eb.night_mode.value = nightMode
        AppCompatDelegate.setDefaultNightMode(nightMode)
        if (restartMainActivity) {
            restartMainActivity()
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

    @SuppressLint("HardwareIds")
    fun getAndroidId(): String {
        return Settings.Secure.getString(ebApp.contentResolver, Settings.Secure.ANDROID_ID).toString()
    }

    fun md5(string: String): String {
        return MessageDigest.getInstance("MD5").digest(string.toByteArray())
            .joinToString("") { (it + 0x200).toString(16).substring(1) }
    }

    fun getUserId(): String {
        return md5(getAndroidId())
    }

    fun base64Encode(string: String): String {
        return Base64.encodeToString(string.toByteArray(), Base64.NO_WRAP)
    }

    fun base64Decode(string: String): String {
        return String(Base64.decode(string, Base64.NO_WRAP))
    }

    fun clip(
        context: Context,
        text: CharSequence,
        toast: CharSequence = context.getString(R.string.eb_clip),
        label: CharSequence = ebApp.packageName
    ) {
        SystemServices.clipboardManager.primaryClip = ClipData.newPlainText(label, text)
        toast(context, toast)
    }
}
