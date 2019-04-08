package com.ebnbin.windowcamera.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.view.Surface
import android.view.TextureView
import android.view.WindowManager
import android.widget.FrameLayout
import com.ebnbin.eb.sharedpreferences.getSharedPreferences
import com.ebnbin.eb.util.Ratio
import com.ebnbin.eb.util.cameraManager
import com.ebnbin.eb.util.displayRealSize
import com.ebnbin.eb.util.toast
import com.ebnbin.eb.util.windowManager
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.camera.CameraHelper
import com.ebnbin.windowcamera.profile.ProfileHelper
import com.ebnbin.windowcamera.service.WindowCameraService

/**
 * 用于 WindowCameraService 添加到 WindowManager 上的 view.
 *
 * TextureView 不支持 onDraw 或 onDrawForeground, 使用 FrameLayout 包装, 在 onDrawForeground 绘制自定义内容.
 */
class WindowCameraView(context: Context) : FrameLayout(context), TextureView.SurfaceTextureListener,
    SharedPreferences.OnSharedPreferenceChangeListener {
    private val textureView: TextureView = TextureView(this.context)

    init {
        setWillNotDraw(false)

        textureView.surfaceTextureListener = this
        addView(textureView)
    }

    //*****************************************************************************************************************

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        getSharedPreferences().registerOnSharedPreferenceChangeListener(this)

        invalidateSize()
        invalidateCamera()
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
            ProfileHelper.KEY_IS_OUT_ENABLED -> {
                // TODO
            }
            ProfileHelper.KEY_IN_X -> {
                // TODO
            }
            ProfileHelper.KEY_IN_Y -> {
                // TODO
            }
            ProfileHelper.KEY_OUT_X -> {
                // TODO
            }
            ProfileHelper.KEY_OUT_Y -> {
                // TODO
            }
            ProfileHelper.KEY_SIZE -> {
                invalidateSize()
            }
            ProfileHelper.KEY_RATIO -> {
                invalidateSize()
            }
            ProfileHelper.KEY_IS_FRONT -> {
                closeCamera()
                invalidateCamera()
                openCamera()
            }
        }
    }

    //*****************************************************************************************************************

    private fun updateLayoutParams(block: WindowManager.LayoutParams.() -> Unit) {
        val params = layoutParams as WindowManager.LayoutParams
        block(params)
        windowManager.updateViewLayout(this, params)
    }

    private fun invalidateSize() {
        updateLayoutParams {
            val displayRealSize = displayRealSize
            val sizeSp = ProfileHelper.size
            val ratioSp = ProfileHelper.ratio
            val ratio = when (ratioSp) {
                "capture" -> {
                    // TODO
                    ProfileHelper.device().maxResolution.ratio
                }
                "raw" -> ProfileHelper.device().maxResolution.ratio
                "screen" -> displayRealSize.ratio
                "square" -> Ratio.SQUARE
                else -> throw RuntimeException()
            }
            val rotationSize = displayRealSize.crop(ratio, sizeSp)
            width = rotationSize.width
            height = rotationSize.height
        }
    }

    //*****************************************************************************************************************

    private lateinit var device: CameraHelper.Device

    private fun invalidateCamera() {
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

        val surface = Surface(textureView.surfaceTexture)
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

    //*****************************************************************************************************************

    override fun onDrawForeground(canvas: Canvas?) {
        super.onDrawForeground(canvas)
    }
}
