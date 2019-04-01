package com.ebnbin.windowcamera.camera

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.util.AttributeSet
import android.view.Surface
import android.view.TextureView
import com.ebnbin.eb.sharedpreferences.getSharedPreferences
import com.ebnbin.eb.util.cameraManager
import com.ebnbin.eb.util.toast
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.profile.ProfileHelper
import com.ebnbin.windowcamera.service.WindowCameraService

/**
 * 相机 TextureView.
 */
class CameraView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : TextureView(context, attrs, defStyleAttr, defStyleRes),
    TextureView.SurfaceTextureListener,
    SharedPreferences.OnSharedPreferenceChangeListener {
    init {
        surfaceTextureListener = this
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        getSharedPreferences().registerOnSharedPreferenceChangeListener(this)

        invalidateProfile()
    }

    override fun onDetachedFromWindow() {
        getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this)
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

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            ProfileHelper.KEY_IS_FRONT -> {
                closeCamera()
                invalidateProfile()
                openCamera()
            }
        }
    }

    //*****************************************************************************************************************

    private lateinit var device: CameraHelper.Device

    private fun invalidateProfile() {
        device = if (ProfileHelper.isFront) CameraHelper.frontDevice else CameraHelper.backDevice
    }

    //*****************************************************************************************************************

    private var cameraDevice: CameraDevice? = null

    @SuppressLint("MissingPermission")
    private fun openCamera() {
        ProfileHelper.isCameraProfileInvalidating = true

        val callback = object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                cameraDevice = camera

                startPreview()
            }

            override fun onClosed(camera: CameraDevice) {
                super.onClosed(camera)
                cameraDevice = null
            }

            override fun onDisconnected(camera: CameraDevice) {
                // 通常发生在通过别的应用启动相机时.
                // 先将 cameraDevice 设置为 null, 以防在关闭摄像头过程中错误的调用.
                cameraDevice = null
                camera.close()
                // TODO: 更合理的提示.
                onCameraError()
            }

            override fun onError(camera: CameraDevice, error: Int) {
                // 先将 cameraDevice 设置为 null, 以防在关闭摄像头过程中错误的调用.
                cameraDevice = null
                camera.close()
                onCameraError()
            }
        }
        cameraManager.openCamera(device.id, callback, null)
    }

    private fun closeCamera() {
        ProfileHelper.isCameraProfileInvalidating = true

        stopPreview()

        cameraDevice?.run {
            cameraDevice = null
            close()
        }

        ProfileHelper.isCameraProfileInvalidating = false
    }

    //*****************************************************************************************************************

    private var cameraCaptureSession: CameraCaptureSession? = null

    private fun startPreview() {
        val cameraDevice = cameraDevice ?: return

        val surface = Surface(surfaceTexture)
        val outputs = listOf(surface)
        val callback = object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                cameraCaptureSession = session

                val captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                captureRequestBuilder.addTarget(surface)
                val request = captureRequestBuilder.build()
                session.setRepeatingRequest(request, null, null)

                ProfileHelper.isCameraProfileInvalidating = false
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {
                onCameraError()
            }
        }
        cameraDevice.createCaptureSession(outputs, callback, null)
    }

    private fun stopPreview() {
        cameraCaptureSession?.run {
            cameraCaptureSession = null
            try {
                stopRepeating()
            } catch (e: IllegalStateException) {
                // stopRepeating 对应 setRepeatingRequest, 但有可能出现 cameraCaptureSession 已经 closed 的情况.
            }
            close()
        }
    }

    //*****************************************************************************************************************

    private fun onCameraError() {
        WindowCameraService.stop(context)
        toast(context, R.string.camera_error)
    }
}
