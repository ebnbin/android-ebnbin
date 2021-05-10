package com.ebnbin.windowcamera.view.surfacetexture

import android.graphics.SurfaceTexture
import com.ebnbin.windowcamera.view.IWindowCameraViewDelegate

interface IWindowCameraViewSurfaceDelegate : IWindowCameraViewDelegate {
    /**
     * 返回 TextureView.surfaceTexture. 调用时不应该为空.
     */
    fun getSurfaceTexture(): SurfaceTexture?
}
