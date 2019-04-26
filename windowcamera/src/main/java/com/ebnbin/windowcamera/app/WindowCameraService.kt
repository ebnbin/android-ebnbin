package com.ebnbin.windowcamera.app

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import com.ebnbin.eb.library.eventBus
import com.ebnbin.eb.permission.PermissionHelper
import com.ebnbin.eb.util.AppHelper
import com.ebnbin.eb.util.BuildHelper
import com.ebnbin.eb.util.SystemServices
import com.ebnbin.windowcamera.R

/**
 * 前台服务.
 */
class WindowCameraService : Service() {
    private var windowCameraView: WindowCameraView? = null

    override fun onCreate() {
        super.onCreate()
        eventBus.post(WindowCameraServiceEvent(true))

        startForeground()

        windowCameraView = WindowCameraView(this)
        val params = WindowManager.LayoutParams()
        params.type = if (BuildHelper.sdk26O()) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }
        params.gravity = Gravity.START or Gravity.TOP
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        params.format = PixelFormat.TRANSLUCENT
        SystemServices.windowManager.addView(windowCameraView, params)
    }

    private fun startForeground() {
        if (BuildHelper.sdk26O() &&
            SystemServices.notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null) {
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "WindowCameraService",
                NotificationManager.IMPORTANCE_HIGH)
            SystemServices.notificationManager.createNotificationChannel(notificationChannel)
        }
        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(android.R.mipmap.sym_def_app_icon)
            .build()
        startForeground(NOTIFICATION_ID, notification)
    }

    override fun onDestroy() {
        windowCameraView?.run {
            SystemServices.windowManager.removeView(windowCameraView)
            windowCameraView = null
        }

        stopForeground(true)

        eventBus.post(WindowCameraServiceEvent(false))
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "window_camera_service"

        private const val NOTIFICATION_ID = 1

        /**
         * 所需权限.
         */
        val permissions: ArrayList<String> = arrayListOf(
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )

        fun start(context: Context) {
            if (isRunning()) return
            if (!PermissionHelper.isPermissionsGranted(permissions)) {
                AppHelper.toast(context, R.string.eb_permission_denied)
                return
            }
            val intent = Intent(context, WindowCameraService::class.java)
            if (BuildHelper.sdk26O()) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stop(context: Context) {
            if (!isRunning()) return
            val intent = Intent(context, WindowCameraService::class.java)
            context.stopService(intent)
        }

        fun isRunning(): Boolean {
            return AppHelper.isServiceRunning(WindowCameraService::class.java)
        }
    }
}
