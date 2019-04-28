package com.ebnbin.windowcamera.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class StopWindowCameraServiceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        WindowCameraService.stop(context)
    }
}
