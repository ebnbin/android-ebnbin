package com.ebnbin.eb2.util

import android.app.ActivityManager
import android.app.DownloadManager
import android.app.NotificationManager
import android.content.ClipboardManager
import android.content.pm.ShortcutManager
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Vibrator
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService

object SystemServices {
    val activityManager: ActivityManager
        get() = ebApp.getSystemService() ?: throw RuntimeException()

    val cameraManager: CameraManager
        get() = ebApp.getSystemService() ?: throw RuntimeException()

    val clipboardManager: ClipboardManager
        get() = ebApp.getSystemService() ?: throw RuntimeException()

    val downloadManager: DownloadManager
        get() = ebApp.getSystemService() ?: throw RuntimeException()

    val notificationManager: NotificationManager
        get() = ebApp.getSystemService() ?: throw RuntimeException()

    val shortcutManager: ShortcutManager
        @RequiresApi(Build.VERSION_CODES.N_MR1)
        get() = ebApp.getSystemService() ?: throw RuntimeException()

    val vibrator: Vibrator
        get() = ebApp.getSystemService() ?: throw RuntimeException()

    val windowManager: WindowManager
        get() = ebApp.getSystemService() ?: throw RuntimeException()
}
