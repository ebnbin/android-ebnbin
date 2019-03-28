package com.ebnbin.windowcamera.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.ebnbin.eb.util.notificationManager
import com.ebnbin.eb.util.sdk26O

/**
 * 前台服务.
 */
class WindowCameraService : Service() {
    override fun onCreate() {
        super.onCreate()
        startForeground()
    }

    private fun startForeground() {
        if (sdk26O() && notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null) {
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID,
                WindowCameraService::class.java.simpleName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(android.R.mipmap.sym_def_app_icon)
            .build()
        startForeground(NOTIFICATION_ID, notification)
    }

    override fun onDestroy() {
        stopForeground(true)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "window_camera_service"

        private const val NOTIFICATION_ID = 1

        fun start(context: Context) {
            val intent = Intent(context, WindowCameraService::class.java)
            if (sdk26O()) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stop(context: Context) {
            val intent = Intent(context, WindowCameraService::class.java)
            context.stopService(intent)
        }
    }
}
