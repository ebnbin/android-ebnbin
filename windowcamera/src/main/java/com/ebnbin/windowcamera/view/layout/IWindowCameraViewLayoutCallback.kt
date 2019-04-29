package com.ebnbin.windowcamera.view.layout

import android.view.WindowManager
import com.ebnbin.windowcamera.camera.CameraHelper
import com.ebnbin.windowcamera.view.IWindowCameraViewCallback

interface IWindowCameraViewLayoutCallback : IWindowCameraViewCallback {
    /**
     * 更新 WindowCameraView LayoutParams.
     */
    fun updateLayoutParams(block: WindowManager.LayoutParams.() -> Unit)

    /**
     * 返回当前拍摄分辨率, 可能是照片分辨率或视频配置.
     */
    fun getResolution(): CameraHelper.Device.Resolution

    /**
     * 返回当前最大分辨率.
     */
    fun getMaxResolution(): CameraHelper.Device.Resolution
}
