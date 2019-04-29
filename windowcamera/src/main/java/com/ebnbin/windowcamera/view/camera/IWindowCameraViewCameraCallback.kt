package com.ebnbin.windowcamera.view.camera

import android.graphics.SurfaceTexture
import com.ebnbin.windowcamera.view.IWindowCameraViewCallback

interface IWindowCameraViewCameraCallback : IWindowCameraViewCallback {
    /**
     * 返回 TextureView.surfaceTexture.
     */
    fun getSurfaceTexture(): SurfaceTexture

    /**
     * 更新大小位置, 不会更新 is_out_enabled.
     */
    fun invalidateSizePosition()
}
