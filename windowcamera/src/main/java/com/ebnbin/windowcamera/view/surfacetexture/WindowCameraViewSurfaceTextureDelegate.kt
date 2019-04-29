package com.ebnbin.windowcamera.view.surfacetexture

import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.SurfaceTexture
import android.view.Surface
import android.view.TextureView
import com.ebnbin.eb.util.RotationDetector
import com.ebnbin.eb.util.WindowHelper
import com.ebnbin.windowcamera.view.WindowCameraView
import kotlin.math.max

class WindowCameraViewSurfaceTextureDelegate(private val callback: IWindowCameraViewSurfaceTextureCallback) :
    IWindowCameraViewSurfaceTextureDelegate,
    TextureView.SurfaceTextureListener,
    RotationDetector.Listener
{
    override fun init(windowCameraView: WindowCameraView) {
        RotationDetector.register(this)
    }

    override fun dispose() {
        RotationDetector.unregister(this)
    }

    //*****************************************************************************************************************

    private val textureView: TextureView = TextureView(callback.getContext()).apply {
        surfaceTextureListener = this@WindowCameraViewSurfaceTextureDelegate
        callback.getViewGroup().addView(this)
    }

    override fun getSurfaceTexture(): SurfaceTexture {
        return textureView.surfaceTexture ?: throw RuntimeException()
    }

    //*****************************************************************************************************************

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
        invalidateTransform()
        callback.onSurfaceTextureCreated()
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
        invalidateTransform()
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        callback.onSurfaceTextureDestroyed()
        return true
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
    }

    //*****************************************************************************************************************

    override fun onRotationChanged(oldRotation: Int, newRotation: Int) {
        invalidateTransform()
    }

    //*****************************************************************************************************************

    private fun invalidateTransform() {
        val rotation = WindowHelper.displayRotation
        val previewResolution = callback.getPreviewResolution()

        val viewWidth = textureView.width.toFloat()
        val viewHeight = textureView.height.toFloat()

        val viewCenterX = 0.5f * viewWidth
        val viewCenterY = 0.5f * viewHeight

        val bufferWidth = previewResolution.widths.getValue(Surface.ROTATION_0)
        val bufferHeight = previewResolution.heights.getValue(Surface.ROTATION_0)

        val viewRectF = RectF(0f, 0f, viewWidth, viewHeight)

        val bufferLeft = 0.5f * (viewWidth - bufferWidth)
        val bufferTop = 0.5f * (viewHeight - bufferHeight)
        val bufferRight = bufferLeft + bufferWidth
        val bufferBottom = bufferTop + bufferHeight
        val bufferRectF = RectF(bufferLeft, bufferTop, bufferRight, bufferBottom)

        getSurfaceTexture().setDefaultBufferSize(bufferWidth, bufferHeight)

        val matrix = Matrix()

        matrix.setRectToRect(viewRectF, bufferRectF, Matrix.ScaleToFit.FILL)

        val scaleX = viewWidth / previewResolution.widths.getValue(rotation)
        val scaleY = viewHeight / previewResolution.heights.getValue(rotation)
        val scale = max(scaleX, scaleY)
        matrix.postScale(scale, scale, viewCenterX, viewCenterY)

        val rotate = 360f - 90f * rotation
        matrix.postRotate(rotate, viewCenterX, viewCenterY)

        textureView.setTransform(matrix)
    }
}
