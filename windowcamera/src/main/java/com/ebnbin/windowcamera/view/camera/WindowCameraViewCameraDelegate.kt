package com.ebnbin.windowcamera.view.camera

import android.Manifest
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
import com.ebnbin.eb.permission.PermissionHelper
import com.ebnbin.eb.util.AppHelper
import com.ebnbin.eb.util.SystemServices
import com.ebnbin.eb.util.WindowHelper
import com.ebnbin.eb.util.res
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.camera.exception.CameraDisconnectedException
import com.ebnbin.windowcamera.camera.exception.CameraException
import com.ebnbin.windowcamera.camera.exception.CameraMediaRecorderStopException
import com.ebnbin.windowcamera.camera.exception.CameraPermissionException
import com.ebnbin.windowcamera.camera.exception.CameraPhotoPreviewStopRepeatingException
import com.ebnbin.windowcamera.camera.exception.CameraVideoCaptureStopRepeatingException
import com.ebnbin.windowcamera.profile.CameraState
import com.ebnbin.windowcamera.profile.ProfileHelper
import com.ebnbin.windowcamera.service.WindowCameraService
import com.ebnbin.windowcamera.util.IOHelper
import java.io.File
import java.io.FileOutputStream

class WindowCameraViewCameraDelegate(private val callback: IWindowCameraViewCameraCallback) :
    IWindowCameraViewCameraDelegate,
    SharedPreferences.OnSharedPreferenceChangeListener
{
    override fun init() {
        ProfileHelper.sharedPreferencesRegister(this)
    }

    override fun dispose() {
        ProfileHelper.sharedPreferencesUnregister(this)
    }

    //*****************************************************************************************************************

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            ProfileHelper.is_front.key -> {
                reopenCamera(false)
            }
            ProfileHelper.is_video.key -> {
                reopenCamera(true)
            }
            ProfileHelper.back_photo_resolution.key -> {
                reopenCamera(false)
            }
            ProfileHelper.back_video_profile.key -> {
                reopenCamera(false)
            }
            ProfileHelper.front_photo_resolution.key -> {
                reopenCamera(false)
            }
            ProfileHelper.front_video_profile.key -> {
                reopenCamera(false)
            }
        }
    }

    //*****************************************************************************************************************

    private var cameraDevice: CameraDevice? = null

    @SuppressLint("MissingPermission")
    override fun openCamera() {
        ProfileHelper.cameraState = CameraState.OPENING

        val callback = object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                cameraDevice = camera
                if (ProfileHelper.is_video.value) {
                    startVideoPreview()
                } else {
                    startPhotoPreview()
                }
            }

            override fun onClosed(camera: CameraDevice) {
                super.onClosed(camera)
                cameraDevice = null
            }

            override fun onDisconnected(camera: CameraDevice) {
                onCameraError(CameraDisconnectedException())
            }

            override fun onError(camera: CameraDevice, error: Int) {
                onCameraError(res.getString(R.string.camera_error_code, error))
            }
        }
        if (PermissionHelper.isPermissionsGranted(arrayListOf(Manifest.permission.CAMERA))) {
            SystemServices.cameraManager.openCamera(ProfileHelper.device().id, callback, null)
        } else {
            onCameraError(CameraPermissionException())
        }
    }

    override fun closeCamera() {
        closeCamera(false)
    }

    private fun closeCamera(isVideoChanged: Boolean) {
        if (isVideoChanged) {
            if (ProfileHelper.is_video.value) {
                stopPhotoPreview()
            } else {
                stopVideoPreview()
            }
        } else {
            if (ProfileHelper.is_video.value) {
                stopVideoPreview()
            } else {
                stopPhotoPreview()
            }
        }

        cameraDevice?.run {
            cameraDevice = null
            close()
        }

        ProfileHelper.cameraState = CameraState.CLOSED
    }

    private fun reopenCamera(isVideoChanged: Boolean) {
        closeCamera(isVideoChanged)
        callback.invalidateSizePosition()
        openCamera()
    }

    //*****************************************************************************************************************

    private var imageReader: ImageReader? = null
    private var photoCameraCaptureSession: CameraCaptureSession? = null

    private fun startPhotoPreview() {
        ProfileHelper.cameraState = CameraState.STARTING_PHOTO_PREVIEW

        val cameraDevice = cameraDevice
        if (cameraDevice == null) {
            onCameraError(callback.getContext().getString(R.string.camera_error_null))
            return
        }
        val photoResolution = ProfileHelper.photoResolution()
        val imageReader = ImageReader.newInstance(photoResolution.width, photoResolution.height, ImageFormat.JPEG, 2)
        val onImageAvailableListener = ImageReader.OnImageAvailableListener {
            val image = it.acquireNextImage() ?: return@OnImageAvailableListener
            val byteBuffer = image.planes[0].buffer
            val byteArray = ByteArray(byteBuffer.remaining())
            byteBuffer.get(byteArray)
            val file = IOHelper.nextFile(".jpg")
            val fos = FileOutputStream(file)
            fos.write(byteArray)
            fos.close()
            image.close()
            toastFile(file)
        }
        imageReader.setOnImageAvailableListener(onImageAvailableListener, null)
        this.imageReader = imageReader

        val surfaceTextureSurface = Surface(callback.getSurfaceTexture())
        val imageReaderSurface = imageReader.surface
        val outputs = listOf(surfaceTextureSurface, imageReaderSurface)
        val callback = object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                val innerCameraDevice = this@WindowCameraViewCameraDelegate.cameraDevice
                if (innerCameraDevice == null) {
                    onCameraError(CameraException(""))
                    return
                }
                val captureRequestBuilder = innerCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                captureRequestBuilder.addTarget(surfaceTextureSurface)
                val request = captureRequestBuilder.build()
                session.setRepeatingRequest(request, null, null)
                photoCameraCaptureSession = session

                ProfileHelper.cameraState = CameraState.PREVIEWING_PHOTO
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {
                onCameraError(res.getString(R.string.camera_error_photo_preview))
            }
        }
        cameraDevice.createCaptureSession(outputs, callback, null)
    }

    private fun stopPhotoPreview() {
        photoCameraCaptureSession?.run {
            photoCameraCaptureSession = null
            try {
                stopRepeating()
            } catch (e: Exception) {
                DevHelper.report(CameraPhotoPreviewStopRepeatingException(e))
            }
            try {
                close()
            } catch (e: Exception) {
                DevHelper.report(e)
            }
        }

        imageReader?.run {
            imageReader = null
            try {
                close()
            } catch (e: Exception) {
                DevHelper.report(e)
            }
        }
    }

    private fun photoCapture() {
        val cameraDevice = cameraDevice
        val imageReader = imageReader
        val photoCameraCaptureSession = photoCameraCaptureSession
        if (cameraDevice == null || imageReader == null || photoCameraCaptureSession == null) {
            onCameraError(callback.getContext().getString(R.string.camera_error_null))
            return
        }

        val imageReaderSurface = imageReader.surface
        val captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
        captureRequestBuilder.addTarget(imageReaderSurface)
        captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION,
            ProfileHelper.device().sensorOrientations[WindowHelper.displayRotation])
        val request = captureRequestBuilder.build()
        photoCameraCaptureSession.capture(request, null, null)
    }

    //*****************************************************************************************************************

    private var videoPreviewCameraCaptureSession: CameraCaptureSession? = null

    private fun startVideoPreview() {
        ProfileHelper.cameraState = CameraState.STARTING_VIDEO_PREVIEW

        val cameraDevice = cameraDevice
        if (cameraDevice == null) {
            onCameraError(callback.getContext().getString(R.string.camera_error_null))
            return
        }
        val surfaceTextureSurface = Surface(callback.getSurfaceTexture())
        val outputs = listOf(surfaceTextureSurface)
        val callback = object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                val innerCameraDevice = this@WindowCameraViewCameraDelegate.cameraDevice
                if (innerCameraDevice == null) {
                    onCameraError(CameraException(""))
                    return
                }
                val captureRequestBuilder = innerCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                captureRequestBuilder.addTarget(surfaceTextureSurface)
                val request = captureRequestBuilder.build()
                session.setRepeatingRequest(request, null, null)
                videoPreviewCameraCaptureSession = session

                ProfileHelper.cameraState = CameraState.PREVIEWING_VIDEO
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {
                onCameraError(res.getString(R.string.camera_error_video_preview))
            }
        }
        cameraDevice.createCaptureSession(outputs, callback, null)
    }

    private fun stopVideoPreview() {
        stopVideoCapture(false)

        videoPreviewCameraCaptureSession?.run {
            videoPreviewCameraCaptureSession = null
            try {
                stopRepeating()
            } catch (e: Exception) {
                DevHelper.report(e)
            }
            try {
                close()
            } catch (e: Exception) {
                DevHelper.report(e)
            }
        }
    }

    private var videoFile: File? = null
    private var mediaRecorder: MediaRecorder? = null
    private var videoCaptureCameraCaptureSession: CameraCaptureSession? = null
    private var isVideoRecording: Boolean = false

    private fun startVideoCapture() {
        val cameraDevice = cameraDevice
        if (cameraDevice == null) {
            onCameraError(callback.getContext().getString(R.string.camera_error_null))
            return
        }
        stopVideoPreview()
        val videoProfile = ProfileHelper.videoProfile()
        val videoFile = IOHelper.nextFile(videoProfile.extension)
        val mediaRecorder = MediaRecorder()
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE)
        mediaRecorder.setProfile(videoProfile.camcorderProfile)
        mediaRecorder.setOutputFile(videoFile.absolutePath)
        mediaRecorder.setOrientationHint(ProfileHelper.device().sensorOrientations[WindowHelper.displayRotation])
        mediaRecorder.prepare()
        this.videoFile = videoFile
        this.mediaRecorder = mediaRecorder

        val surfaceTextureSurface = Surface(callback.getSurfaceTexture())
        val mediaRecorderSurface = mediaRecorder.surface
        val outputs = listOf(surfaceTextureSurface, mediaRecorderSurface)
        val callback = object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                val innerCameraDevice = this@WindowCameraViewCameraDelegate.cameraDevice
                if (innerCameraDevice == null) {
                    onCameraError(CameraException(""))
                    return
                }
                val captureRequestBuilder = innerCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD)
                captureRequestBuilder.addTarget(surfaceTextureSurface)
                captureRequestBuilder.addTarget(mediaRecorderSurface)
                val request = captureRequestBuilder.build()
                session.setRepeatingRequest(request, null, null)
                videoCaptureCameraCaptureSession = session
                mediaRecorder.start()
                isVideoRecording = true

                ProfileHelper.cameraState = CameraState.CAPTURING_VIDEO
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {
                onCameraError(res.getString(R.string.camera_error_video_capture))
            }
        }
        cameraDevice.createCaptureSession(outputs, callback, null)
    }

    private fun stopVideoCapture(resumePreview: Boolean) {
        if (!isVideoRecording) return
        isVideoRecording = false

        var error = false
        mediaRecorder?.run {
            mediaRecorder = null
            try {
                stop()
            } catch (e: Exception) {
                DevHelper.report(CameraMediaRecorderStopException(e))
                error = true
            }
            try {
                release()
            } catch (e: Exception) {
                DevHelper.report(e)
                error = true
            }
        }
        videoFile?.run {
            videoFile = null
            if (error) {
                AppHelper.toast(callback.getContext(), R.string.camera_error_media_recorder_stop)
            } else {
                toastFile(this)
            }
        }

        videoCaptureCameraCaptureSession?.run {
            videoCaptureCameraCaptureSession = null
            try {
                stopRepeating()
            } catch (e: Exception) {
                DevHelper.report(CameraVideoCaptureStopRepeatingException(e))
            }
            try {
                close()
            } catch (e: Exception) {
                DevHelper.report(e)
            }
        }

        if (resumePreview) {
            startVideoPreview()
        }
    }

    //*****************************************************************************************************************

    override fun capture() {
        if (ProfileHelper.is_video.value) {
            if (isVideoRecording) {
                stopVideoCapture(true)
            } else {
                startVideoCapture()
            }
        } else {
            photoCapture()
        }
    }

    private fun toastFile(file: File) {
        AppHelper.toast(callback.getContext(), file)
    }

    //*****************************************************************************************************************

    /**
     * 相机异常.
     */
    private fun onCameraError(string: String) {
        DevHelper.report(CameraException(string))
        AppHelper.toast(callback.getContext(), string)
        WindowCameraService.stop(callback.getContext())
    }

    /**
     * 相机异常.
     */
    private fun onCameraError(exception: CameraException) {
        DevHelper.report(exception)
        if (exception.text.isNotEmpty()) {
            AppHelper.toast(callback.getContext(), exception.text)
        }
        WindowCameraService.stop(callback.getContext())
    }
}
