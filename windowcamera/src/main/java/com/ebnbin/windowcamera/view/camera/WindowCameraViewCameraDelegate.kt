package com.ebnbin.windowcamera.view.camera

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.ImageFormat
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CaptureRequest
import android.media.ImageReader
import android.media.MediaRecorder
import android.view.Surface
import com.ebnbin.eb.dev.DevHelper
import com.ebnbin.eb.util.AppHelper
import com.ebnbin.eb.util.ResHelper
import com.ebnbin.eb.util.SystemServices
import com.ebnbin.eb.util.WindowHelper
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.camera.exception.CameraReportException
import com.ebnbin.windowcamera.profile.CameraState
import com.ebnbin.windowcamera.profile.ProfileHelper
import com.ebnbin.windowcamera.service.WindowCameraService
import com.ebnbin.windowcamera.util.IOHelper
import java.io.File

class WindowCameraViewCameraDelegate(private val callback: IWindowCameraViewCameraCallback) :
    IWindowCameraViewCameraDelegate,
    SharedPreferences.OnSharedPreferenceChangeListener
{
    override fun init() {
        ProfileHelper.sharedPreferencesRegister(this)

        ProfileHelper.cameraState = CameraState.STATING
    }

    override fun dispose() {
        ProfileHelper.cameraState = CameraState.CLOSED

        ProfileHelper.sharedPreferencesUnregister(this)
    }

    //*****************************************************************************************************************

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            ProfileHelper.is_front.key -> {
                reopenCamera()
            }
            ProfileHelper.is_preview.key -> {
                reopenCamera(isPreviewChanged = true)
            }
            ProfileHelper.is_video.key -> {
                reopenCamera(isVideoChanged = true)
            }
            ProfileHelper.back_photo_resolution.key -> {
                reopenCamera()
            }
            ProfileHelper.back_video_profile.key -> {
                reopenCamera()
            }
            ProfileHelper.front_photo_resolution.key -> {
                reopenCamera()
            }
            ProfileHelper.front_video_profile.key -> {
                reopenCamera()
            }
        }
    }

    //*****************************************************************************************************************

    private var cameraDevice: CameraDevice? = null

    @SuppressLint("MissingPermission")
    override fun openCamera() {
        val callback = object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                cameraDevice = camera
                if (ProfileHelper.is_preview.value) {
                    startOnlyPreview()
                } else {
                    if (ProfileHelper.is_video.value) {
                        startVideoPreview()
                    } else {
                        startPhotoPreview()
                    }
                }
            }

            override fun onClosed(camera: CameraDevice) {
                super.onClosed(camera)
                cameraDevice = null
            }

            override fun onDisconnected(camera: CameraDevice) {
                // 通常发生在通过别的应用启动相机时.
                onCameraError(R.string.camera_error_disconnected)
            }

            override fun onError(camera: CameraDevice, error: Int) {
                onCameraError(callback.getContext().getString(R.string.camera_error_code, error))
            }
        }
        try {
            SystemServices.cameraManager.openCamera(ProfileHelper.device().id, callback, null)
        } catch (e: Exception) {
            onCameraError(R.string.camera_error_open, e)
            return
        }
    }

    override fun closeCamera() {
        internalCloseCamera(onIsPreviewChanged = false, onIsVideoChanged = false)
    }

    private fun internalCloseCamera(onIsPreviewChanged: Boolean, onIsVideoChanged: Boolean) {
        ProfileHelper.cameraState = CameraState.STATING

        var isPreview = ProfileHelper.is_preview.value
        var isVideo = ProfileHelper.is_video.value
        if (onIsPreviewChanged) isPreview = !isPreview
        if (onIsVideoChanged) isVideo = !isVideo
        if (isPreview) {
            stopOnlyPreview()
        } else {
            if (isVideo) {
                stopVideoPreview()
            } else {
                stopPhotoPreview()
            }
        }

        cameraDevice?.run {
            cameraDevice = null
            close()
        }
    }

    private fun reopenCamera(isPreviewChanged: Boolean = false, isVideoChanged: Boolean = false) {
        internalCloseCamera(isPreviewChanged, isVideoChanged)
        callback.invalidateSizePosition()
        openCamera()
    }

    //*****************************************************************************************************************

    private var cameraCaptureSession: CameraCaptureSession? = null

    //*****************************************************************************************************************

    private var imageReader: ImageReader? = null

    private fun startPhotoPreview() {
        val cameraDevice = cameraDevice
        if (cameraDevice == null) {
            onCameraError(R.string.camera_error_camera_device_null)
            return
        }

        val surfaceTexture = callback.getSurfaceTexture()
        if (surfaceTexture == null) {
            onCameraError(R.string.camera_error_surface_texture_null)
            return
        }
        val surface = Surface(surfaceTexture)

        val photoResolution = ProfileHelper.photoResolution()
        val imageReader = ImageReader.newInstance(photoResolution.width, photoResolution.height, ImageFormat.JPEG, 2)
        val listener = ImageReader.OnImageAvailableListener {
            val image = it.acquireNextImage() ?: return@OnImageAvailableListener
            val byteBuffer = image.planes[0].buffer
            val byteArray = ByteArray(byteBuffer.remaining())
            byteBuffer.get(byteArray)
            val file = IOHelper.nextFile(".jpg")
            file.writeBytes(byteArray)
            image.close()
            callback.toast(file)
        }
        imageReader.setOnImageAvailableListener(listener, null)
        val imageReaderSurface = imageReader.surface
        this.imageReader = imageReader

        val outputs = listOf(surface, imageReaderSurface)
        val callback = object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                if (ProfileHelper.is_preview.value || ProfileHelper.is_video.value) {
                    onCameraError(R.string.camera_error_state, toast = false, stopService = false)
                    return
                }

                val innerCameraDevice = this@WindowCameraViewCameraDelegate.cameraDevice
                if (innerCameraDevice == null) {
                    onCameraError(R.string.camera_error_camera_device_null)
                    return
                }

                val captureRequestBuilder = innerCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                captureRequestBuilder.addTarget(surface)
                val request = captureRequestBuilder.build()
                session.setRepeatingRequest(request, null, null)
                cameraCaptureSession = session

                ProfileHelper.cameraState = CameraState.PREVIEWING_PHOTO
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {
                onCameraError(R.string.camera_error_camera_capture_session_configure_failed)
            }
        }
        try {
            cameraDevice.createCaptureSession(outputs, callback, null)
        } catch (e: Exception) {
            onCameraError(R.string.camera_error_create_capture_session, e)
        }
    }

    private fun stopPhotoPreview() {
        cameraCaptureSession?.run {
            cameraCaptureSession = null
            try {
                stopRepeating()
            } catch (e: Exception) {
                onCameraError(R.string.camera_error_stop_repeating, e, toast = false, stopService = false)
            }
            close()
        }

        imageReader?.run {
            imageReader = null
            close()
        }
    }

    private fun photoCapture() {
        val cameraDevice = cameraDevice
        if (cameraDevice == null) {
            onCameraError(R.string.camera_error_camera_device_null)
            return
        }

        val cameraCaptureSession = cameraCaptureSession
        if (cameraCaptureSession == null) {
            onCameraError(R.string.camera_error_photo_capture_init)
            return
        }

        val imageReader = imageReader
        if (imageReader == null) {
            onCameraError(R.string.camera_error_photo_capture_init)
            return
        }
        val imageReaderSurface = imageReader.surface

        val captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
        captureRequestBuilder.addTarget(imageReaderSurface)
        captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION,
            ProfileHelper.device().sensorOrientations[WindowHelper.displayRotation])
        val request = captureRequestBuilder.build()
        cameraCaptureSession.capture(request, null, null)
    }

    //*****************************************************************************************************************

    private fun startVideoPreview() {
        val cameraDevice = cameraDevice
        if (cameraDevice == null) {
            onCameraError(R.string.camera_error_camera_device_null)
            return
        }

        val surfaceTexture = callback.getSurfaceTexture()
        if (surfaceTexture == null) {
            onCameraError(R.string.camera_error_surface_texture_null)
            return
        }
        val surface = Surface(surfaceTexture)

        val outputs = listOf(surface)
        val callback = object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                if (ProfileHelper.is_preview.value || !ProfileHelper.is_video.value) {
                    onCameraError(R.string.camera_error_state, toast = false, stopService = false)
                    return
                }

                val innerCameraDevice = this@WindowCameraViewCameraDelegate.cameraDevice
                if (innerCameraDevice == null) {
                    onCameraError(R.string.camera_error_camera_device_null)
                    return
                }

                val captureRequestBuilder = innerCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                captureRequestBuilder.addTarget(surface)
                val request = captureRequestBuilder.build()
                session.setRepeatingRequest(request, null, null)
                cameraCaptureSession = session

                ProfileHelper.cameraState = CameraState.PREVIEWING_VIDEO
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {
                onCameraError(R.string.camera_error_camera_capture_session_configure_failed)
            }
        }
        try {
            cameraDevice.createCaptureSession(outputs, callback, null)
        } catch (e: Exception) {
            onCameraError(R.string.camera_error_create_capture_session, e)
        }
    }

    private fun stopVideoPreview() {
        stopVideoCapture(false)

        cameraCaptureSession?.run {
            cameraCaptureSession = null
            try {
                stopRepeating()
            } catch (e: Exception) {
                onCameraError(R.string.camera_error_stop_repeating, e, toast = false, stopService = false)
            }
            close()
        }
    }

    private var mediaRecorder: MediaRecorder? = null
    private var videoFile: File? = null
    private var isVideoRecording: Boolean = false

    private fun startVideoCapture() {
        stopVideoPreview()

        val cameraDevice = cameraDevice
        if (cameraDevice == null) {
            onCameraError(R.string.camera_error_camera_device_null)
            return
        }

        val surfaceTexture = callback.getSurfaceTexture()
        if (surfaceTexture == null) {
            onCameraError(R.string.camera_error_surface_texture_null)
            return
        }
        val surface = Surface(surfaceTexture)

        val videoProfile = ProfileHelper.videoProfile()
        val videoFile = IOHelper.nextFile(videoProfile.extension)
        val mediaRecorder = MediaRecorder()
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE)
        mediaRecorder.setProfile(videoProfile.camcorderProfile)
        mediaRecorder.setOutputFile(videoFile.absolutePath)
        mediaRecorder.setOrientationHint(ProfileHelper.device().sensorOrientations[WindowHelper.displayRotation])
        val mediaRecorderSurface = try {
            mediaRecorder.prepare()
            mediaRecorder.surface ?: throw NullPointerException()
        } catch (e: Exception) {
            onCameraError(R.string.camera_error_video_capture_init, e)
            return
        }
        this.mediaRecorder = mediaRecorder
        this.videoFile = videoFile

        val outputs = listOf(surface, mediaRecorderSurface)
        val callback = object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                if (ProfileHelper.is_preview.value || !ProfileHelper.is_video.value) {
                    onCameraError(R.string.camera_error_state, toast = false, stopService = false)
                    return
                }

                val innerCameraDevice = this@WindowCameraViewCameraDelegate.cameraDevice
                if (innerCameraDevice == null) {
                    onCameraError(R.string.camera_error_camera_device_null)
                    return
                }

                val captureRequestBuilder = innerCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD)
                captureRequestBuilder.addTarget(surface)
                captureRequestBuilder.addTarget(mediaRecorderSurface)
                val request = captureRequestBuilder.build()
                session.setRepeatingRequest(request, null, null)
                cameraCaptureSession = session

                mediaRecorder.start()
                isVideoRecording = true

                ProfileHelper.cameraState = CameraState.CAPTURING_VIDEO
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {
                onCameraError(R.string.camera_error_camera_capture_session_configure_failed)
            }
        }
        try {
            cameraDevice.createCaptureSession(outputs, callback, null)
        } catch (e: Exception) {
            onCameraError(R.string.camera_error_create_capture_session, e)
        }
    }

    private fun stopVideoCapture(restartPreview: Boolean) {
        if (isVideoRecording) {
            isVideoRecording = false

            cameraCaptureSession?.run {
                cameraCaptureSession = null
                try {
                    stopRepeating()
                } catch (e: Exception) {
                    onCameraError(R.string.camera_error_stop_repeating, e, toast = false, stopService = false)
                }
                close()
            }

            mediaRecorder?.run {
                mediaRecorder = null
                try {
                    stop()
                } catch (e: Exception) {
                    onCameraError(R.string.camera_error_video_stop, e, toast = false, stopService = false)
                    videoFile?.run {
                        videoFile = null
                        delete()
                    }
                }
                release()
            }

            videoFile?.run {
                videoFile = null
                callback.toast(this)
            }
        }

        if (restartPreview) {
            startVideoPreview()
        }
    }

    //*****************************************************************************************************************

    private fun startOnlyPreview() {
        val cameraDevice = cameraDevice
        if (cameraDevice == null) {
            onCameraError(R.string.camera_error_camera_device_null)
            return
        }

        val surfaceTexture = callback.getSurfaceTexture()
        if (surfaceTexture == null) {
            onCameraError(R.string.camera_error_surface_texture_null)
            return
        }
        val surface = Surface(surfaceTexture)

        val outputs = listOf(surface)
        val callback = object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                if (!ProfileHelper.is_preview.value) {
                    onCameraError(R.string.camera_error_state, toast = false, stopService = false)
                    return
                }

                val innerCameraDevice = this@WindowCameraViewCameraDelegate.cameraDevice
                if (innerCameraDevice == null) {
                    onCameraError(R.string.camera_error_camera_device_null)
                    return
                }

                val captureRequestBuilder = innerCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                captureRequestBuilder.addTarget(surface)
                val request = captureRequestBuilder.build()
                session.setRepeatingRequest(request, null, null)
                cameraCaptureSession = session

                ProfileHelper.cameraState = CameraState.PREVIEWING_ONLY
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {
                onCameraError(R.string.camera_error_camera_capture_session_configure_failed)
            }
        }
        try {
            cameraDevice.createCaptureSession(outputs, callback, null)
        } catch (e: Exception) {
            onCameraError(R.string.camera_error_create_capture_session, e)
        }
    }

    private fun stopOnlyPreview() {
        cameraCaptureSession?.run {
            cameraCaptureSession = null
            try {
                stopRepeating()
            } catch (e: Exception) {
                onCameraError(R.string.camera_error_stop_repeating, e, toast = false, stopService = false)
            }
            close()
        }
    }

    //*****************************************************************************************************************

    override fun capture() {
        if (ProfileHelper.is_preview.value) {
            // Do nothing.
        } else {
            if (ProfileHelper.is_video.value) {
                ProfileHelper.cameraState = CameraState.STATING

                if (isVideoRecording) {
                    stopVideoCapture(true)
                } else {
                    startVideoCapture()
                }
            } else {
                photoCapture()
            }
        }
    }

    //*****************************************************************************************************************

    /**
     * 相机异常.
     */
    private fun onCameraError(
        any: Any?,
        throwable: Throwable? = null,
        report: Boolean = true,
        toast: Boolean = true,
        stopService: Boolean = true
    ) {
        val string = ResHelper.getString(any)
        if (report) {
            DevHelper.report(CameraReportException(string, throwable))
        }
        if (toast) {
            AppHelper.toast(callback.getContext(), string)
        }
        if (stopService) {
            WindowCameraService.stop(callback.getContext())
        }
    }
}
