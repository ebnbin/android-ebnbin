package com.ebnbin.windowcamera.view.surfacetexture

import android.view.ViewGroup
import com.ebnbin.windowcamera.view.IWindowCameraViewCallback

interface IWindowCameraViewSurfaceCallback : IWindowCameraViewCallback {
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
}
