package com.ebnbin.eb.util

import android.annotation.SuppressLint
import android.provider.Settings

object DeviceHelper {
    @SuppressLint("HardwareIds")
    val ANDROID_ID: String = Settings.Secure.getString(ebApp.contentResolver, Settings.Secure.ANDROID_ID).toString()

    val DEVICE_ID: String = DataHelper.md5ToString(ANDROID_ID)
}
