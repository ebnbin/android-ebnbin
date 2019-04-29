package com.ebnbin.windowcamera.view.layout

import android.view.WindowManager
import com.ebnbin.windowcamera.view.IWindowCameraViewCallback

interface IWindowCameraViewLayoutCallback : IWindowCameraViewCallback {
    /**
     * 更新 WindowCameraView LayoutParams.
     */
    fun updateLayoutParams(block: WindowManager.LayoutParams.() -> Unit)
}
