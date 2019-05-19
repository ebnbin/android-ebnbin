package com.ebnbin.eb.util

import android.app.ActivityManager
import android.app.DownloadManager
import android.app.NotificationManager
import android.content.ClipboardManager
import android.hardware.camera2.CameraManager
import android.os.Vibrator
import android.view.WindowManager
import androidx.core.content.getSystemService
import com.ebnbin.eb.exception.WTFException

object SystemServices {
    val activityManager: ActivityManager
        get() = ebApp.getSystemService() ?: throw WTFException()

    val cameraManager: CameraManager
        get() = ebApp.getSystemService() ?: throw WTFException()

    val clipboardManager: ClipboardManager
        get() = ebApp.getSystemService() ?: throw WTFException()

    val downloadManager: DownloadManager
        get() = ebApp.getSystemService() ?: throw WTFException()

    val notificationManager: NotificationManager
        get() = ebApp.getSystemService() ?: throw WTFException()

    val vibrator: Vibrator
        get() = ebApp.getSystemService() ?: throw WTFException()

    val windowManager: WindowManager
        get() = ebApp.getSystemService() ?: throw WTFException()
}
