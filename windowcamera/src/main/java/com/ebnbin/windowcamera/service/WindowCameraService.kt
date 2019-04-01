package com.ebnbin.windowcamera.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import com.ebnbin.eb.library.eventBus
import com.ebnbin.eb.permission.PermissionHelper
import com.ebnbin.eb.util.dpToPxRound
import com.ebnbin.eb.util.isServiceRunning
import com.ebnbin.eb.util.notificationManager
import com.ebnbin.eb.util.sdk26O
import com.ebnbin.eb.util.toast
import com.ebnbin.eb.util.windowManager
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.camera.CameraView
import com.ebnbin.windowcamera.event.WindowCameraServiceEvent

/**
 * 前台服务.
 */
class WindowCameraService : Service() {
    private var cameraView: CameraView? = null

    override fun onCreate() {
        super.onCreate()
        eventBus.post(WindowCameraServiceEvent(true))

        startForeground()

        cameraView = CameraView(this)
        val params = WindowManager.LayoutParams()
        params.width = 160f.dpToPxRound
        params.height = 160f.dpToPxRound
        params.type = if (sdk26O()) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        windowManager.addView(cameraView, params)
    }

    private fun startForeground() {
        if (sdk26O() && notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null) {
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "WindowCameraService",
                NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(android.R.mipmap.sym_def_app_icon)
            .build()
        startForeground(NOTIFICATION_ID, notification)
    }

    override fun onDestroy() {
        cameraView?.run {
            windowManager.removeView(cameraView)
            cameraView = null
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
        val permissions: List<String> = listOf(Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.CAMERA)

        fun start(context: Context) {
            if (isRunning()) return
            if (!PermissionHelper.isPermissionsGranted(permissions)) {
                toast(context, R.string.eb_permission_denied)
                return
            }
            val intent = Intent(context, WindowCameraService::class.java)
            if (sdk26O()) {
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
            return isServiceRunning(WindowCameraService::class.java)
        }
    }
}
