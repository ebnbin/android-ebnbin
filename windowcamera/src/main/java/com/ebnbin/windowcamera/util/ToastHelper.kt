package com.ebnbin.windowcamera.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import com.ebnbin.eb.util.BuildHelper
import com.ebnbin.eb.util.SystemServices

object ToastHelper {
    enum class Type {
        DEFAULT,
        SYSTEM_ALERT_WINDOW,
        NONE
    }

    @SuppressLint("ShowToast")
    fun toast(context: Context, any: Any?, type: Type = ToastHelper.Type.DEFAULT, long: Boolean = false) {
        when (type) {
            ToastHelper.Type.DEFAULT -> {
                val duration = if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
                if (any is Int) {
                    Toast.makeText(context, any, duration).show()
                } else {
                    Toast.makeText(context, any.toString(), duration).show()
                }
            }
            ToastHelper.Type.SYSTEM_ALERT_WINDOW -> {
                val toast = if (any is Int) {
                    Toast.makeText(context, any, Toast.LENGTH_SHORT)
                } else {
                    Toast.makeText(context, any.toString(), Toast.LENGTH_SHORT)
                }
                val view = toast.view
                view.tag = ToastHelper::class.java.name
                val params = WindowManager.LayoutParams()
                params.width = ViewGroup.LayoutParams.WRAP_CONTENT
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT
                params.type = if (BuildHelper.sdk26O()) {
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                } else {
                    @Suppress("DEPRECATION")
                    WindowManager.LayoutParams.TYPE_PHONE
                }
                params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                params.format = PixelFormat.TRANSLUCENT
                SystemServices.windowManager.addView(view, params)
                val delay = if (long) 6000L else 3000L
                view.postDelayed({
                    try {
                        SystemServices.windowManager.removeView(view)
                    } catch (e: Exception) {
                        // Ignore.
                    }
                }, delay)
            }
            ToastHelper.Type.NONE -> {
                // Do nothing.
            }
        }
    }
}
