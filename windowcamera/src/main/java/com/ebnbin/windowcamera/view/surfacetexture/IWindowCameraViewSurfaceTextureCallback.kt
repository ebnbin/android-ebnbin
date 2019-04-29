package com.ebnbin.windowcamera.view.surfacetexture

import android.view.ViewGroup
import com.ebnbin.windowcamera.camera.CameraHelper
import com.ebnbin.windowcamera.view.IWindowCameraViewCallback

interface IWindowCameraViewSurfaceTextureCallback : IWindowCameraViewCallback {
    /**
     * 返回 TextureView 要被添加到的 ViewGroup.
     */
    fun getViewGroup(): ViewGroup

    /**
     * 在这里打开相机.
     */
    fun onSurfaceTextureCreated()

    /**
     * 在这里关闭相机.
     */
    fun onSurfaceTextureDestroyed()

    /**
     * 返回预览分辨率.
     */
    fun getPreviewResolution(): CameraHelper.Device.Resolution
}
