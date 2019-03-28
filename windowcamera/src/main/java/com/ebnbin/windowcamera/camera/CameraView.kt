package com.ebnbin.windowcamera.camera

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraDevice
import android.util.AttributeSet
import android.view.TextureView
import com.ebnbin.eb.util.cameraManager
import com.ebnbin.eb.util.toast
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.service.WindowCameraService

/**
 * 相机 TextureView.
 */
class CameraView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : TextureView(context, attrs, defStyleAttr, defStyleRes), TextureView.SurfaceTextureListener {
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        surfaceTextureListener = this
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    //*****************************************************************************************************************

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
        openCamera()
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        closeCamera()
        return true
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
    }

    //*****************************************************************************************************************

    private var cameraId: String = cameraManager.cameraIdList.first()

    //*****************************************************************************************************************

    private var cameraDevice: CameraDevice? = null

    private val cameraDeviceStateCallback: CameraDevice.StateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            cameraDevice = camera
        }

        override fun onClosed(camera: CameraDevice) {
            super.onClosed(camera)
            cameraDevice = null
        }

        override fun onDisconnected(camera: CameraDevice) {
            camera.close()
            onCameraError()
        }

        override fun onError(camera: CameraDevice, error: Int) {
            camera.close()
            onCameraError()
        }
    }

    @SuppressLint("MissingPermission")
    private fun openCamera() {
        cameraManager.openCamera(cameraId, cameraDeviceStateCallback, null)
    }

    private fun closeCamera() {
        cameraDevice?.run {
            close()
            cameraDevice = null
        }
    }

    //*****************************************************************************************************************

    private fun onCameraError() {
        WindowCameraService.stop(context)
        toast(context, R.string.camera_error)
    }
}
