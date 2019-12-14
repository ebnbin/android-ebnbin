package com.ebnbin.windowcamera.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import com.ebnbin.eb2.library.Libraries
import com.ebnbin.eb2.permission.PermissionHelper
import com.ebnbin.eb2.util.AppHelper
import com.ebnbin.eb2.util.BuildHelper
import com.ebnbin.eb2.util.SystemServices
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.profile.ProfileHelper
import com.ebnbin.windowcamera.view.WindowCameraView

/**
 * 前台服务.
 */
class WindowCameraService : Service() {
    private lateinit var broadcastReceiver: BroadcastReceiver

    private var windowCameraView: WindowCameraView? = null

    override fun onCreate() {
        super.onCreate()
        Libraries.eventBus.post(WindowCameraServiceEvent)

        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    Intent.ACTION_SCREEN_OFF -> {
                        if (ProfileHelper.is_stop_when_screen_off_enabled.value) {
                            stop(this@WindowCameraService)
                        }
                    }
                }
            }
        }
        registerReceiver(broadcastReceiver, IntentFilter(Intent.ACTION_SCREEN_OFF))

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
                NotificationManager.IMPORTANCE_DEFAULT)
            SystemServices.notificationManager.createNotificationChannel(notificationChannel)
        }
        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.eb_logo_198)
            .setContentTitle(getString(R.string.window_camera_service_stop))
            .setContentIntent(PendingIntent.getBroadcast(this, 0,
                Intent(this, StopWindowCameraServiceBroadcastReceiver::class.java), 0))
            .build()
        startForeground(NOTIFICATION_ID, notification)
    }

    override fun onDestroy() {
        windowCameraView?.run {
            SystemServices.windowManager.removeView(windowCameraView)
            windowCameraView = null
        }

        stopForeground(true)

        unregisterReceiver(broadcastReceiver)

        Libraries.eventBus.post(WindowCameraServiceEvent)
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
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
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
