package com.ebnbin.windowcamera.view

import android.graphics.SurfaceTexture
import android.view.TextureView

class WindowCameraViewSurfaceDelegate(private val windowCameraView: WindowCameraView) :
    TextureView.SurfaceTextureListener
{
    val textureView: TextureView = TextureView(windowCameraView.context)
    init {
        textureView.surfaceTextureListener = this
        windowCameraView.getViewGroup().addView(textureView)
    }

    //*****************************************************************************************************************

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
        windowCameraView.cameraDelegate.invalidateTransform()
        windowCameraView.cameraDelegate.openCamera()
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
        windowCameraView.cameraDelegate.invalidateTransform()
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        windowCameraView.cameraDelegate.closeCamera()
        return true
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
    }
}
