package com.ebnbin.windowcamera.widget

import android.content.Context
import android.graphics.Canvas
import android.widget.FrameLayout

/**
 * 用于 WindowCameraService 添加到 WindowManager 上的 view.
 */
class WindowCameraView(context: Context) : FrameLayout(context) {
    private val cameraView: CameraView = CameraView(this.context)

    init {
        setWillNotDraw(false)
        addView(cameraView)
    }

    override fun onDrawForeground(canvas: Canvas?) {
        super.onDrawForeground(canvas)
    }
}
