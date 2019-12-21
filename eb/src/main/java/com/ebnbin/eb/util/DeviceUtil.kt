package com.ebnbin.eb.util

import android.annotation.SuppressLint
import android.provider.Settings
import com.ebnbin.eb.EBApp

object DeviceUtil {
    @SuppressLint("HardwareIds")
    val ANDROID_ID: String = Settings.Secure.getString(EBApp.instance.contentResolver, Settings.Secure.ANDROID_ID)
        .toString()

    val DEVICE_ID: String = ANDROID_ID.toByteArray().toMD5String()
}