package com.ebnbin.windowcamera.view.camera

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.ImageFormat
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CaptureRequest
import android.media.ImageReader
import android.media.MediaRecorder
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import com.ebnbin.eb.util.AppHelper
import com.ebnbin.eb.util.SystemServices
import com.ebnbin.eb.util.WindowHelper
import com.ebnbin.eb.util.res
import com.ebnbin.windowcamera.R
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
        startBackgroundThread()
    }

    override fun dispose() {
        stopBackgroundThread()
        ProfileHelper.sharedPreferencesUnregister(this)
    }

    //*****************************************************************************************************************

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            ProfileHelper.is_front.key -> {
                reopenCamera()
            }
            ProfileHelper.is_video.key -> {
                reopenCamera()
            }
            ProfileHelper.back_photo_resolution.key -> {
                restartPreview()
            }
            ProfileHelper.back_video_profile.key -> {
                restartPreview()
            }
            ProfileHelper.front_photo_resolution.key -> {
                restartPreview()
            }
            ProfileHelper.front_video_profile.key -> {
                restartPreview()
            }
        }
    }

    //*****************************************************************************************************************

    private lateinit var backgroundHandler: Handler

    private fun startBackgroundThread() {
        val handlerThread = HandlerThread("window_camera_view_background")
        handlerThread.start()
        backgroundHandler = Handler(handlerThread.looper)
    }

    private fun stopBackgroundThread() {
        backgroundHandler.looper.quitSafely()
    }

    //*****************************************************************************************************************

    private var cameraDevice: CameraDevice? = null

    @SuppressLint("MissingPermission")
    override fun openCamera() {
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
                onCameraError(res.getString(R.string.camera_error_disconnected))
            }

            override fun onError(camera: CameraDevice, error: Int) {
                onCameraError(res.getString(R.string.camera_error_code, error))
            }
        }
        SystemServices.cameraManager.openCamera(ProfileHelper.device().id, callback, null)
    }

    override fun closeCamera() {
        ProfileHelper.isCameraProfileInvalidating = true

        stopPreview()

        cameraDevice?.run {
            cameraDevice = null
            close()
        }

        ProfileHelper.isCameraProfileInvalidating = false
    }

    private fun reopenCamera() {
        closeCamera()
        callback.invalidateSizePosition()
        openCamera()
    }

    //*****************************************************************************************************************

    private fun startPreview() {
        if (ProfileHelper.is_video.value) {
            startVideoPreview()
        } else {
            startPhotoPreview()
        }
    }

    private fun stopPreview() {
        if (ProfileHelper.is_video.value) {
            stopVideoPreview()
        } else {
            stopPhotoPreview()
        }
    }

    private fun restartPreview() {
        if (ProfileHelper.is_video.value) {
            stopVideoPreview()
            callback.invalidateSizePosition()
            startVideoPreview()
        } else {
            stopPhotoPreview()
            callback.invalidateSizePosition()
            startPhotoPreview()
        }
    }

    //*****************************************************************************************************************

    private var imageReader: ImageReader? = null
    private var photoPreviewCameraCaptureSession: CameraCaptureSession? = null

    private fun startPhotoPreview() {
        val cameraDevice = cameraDevice ?: return

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
                val captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                captureRequestBuilder.addTarget(surfaceTextureSurface)
                val request = captureRequestBuilder.build()
                session.setRepeatingRequest(request, null, null)
                photoPreviewCameraCaptureSession = session

                ProfileHelper.isCameraProfileInvalidating = false
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {
                onCameraError(res.getString(R.string.camera_error_photo_preview))
            }
        }
        cameraDevice.createCaptureSession(outputs, callback, null)
    }

    private fun stopPhotoPreview() {
        photoPreviewCameraCaptureSession?.run {
            photoPreviewCameraCaptureSession = null
            try {
                stopRepeating()
            } catch (e: Exception) {
            }
            try {
                close()
            } catch (e: Exception) {
            }
        }

        imageReader?.run {
            imageReader = null
            try {
                close()
            } catch (e: Exception) {
            }
        }
    }

    private fun photoCapture() {
        val cameraDevice = cameraDevice ?: return
        val imageReader = imageReader ?: return
        val photoCameraCaptureSession = photoPreviewCameraCaptureSession ?: return

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
        val cameraDevice = cameraDevice ?: return

        val surfaceTextureSurface = Surface(callback.getSurfaceTexture())
        val outputs = listOf(surfaceTextureSurface)
        val callback = object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                val captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                captureRequestBuilder.addTarget(surfaceTextureSurface)
                val request = captureRequestBuilder.build()
                session.setRepeatingRequest(request, null, null)
                videoPreviewCameraCaptureSession = session

                ProfileHelper.isCameraProfileInvalidating = false
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
            }
            try {
                close()
            } catch (e: Exception) {
            }
        }
    }

    private var videoFile: File? = null
    private var mediaRecorder: MediaRecorder? = null
    private var videoCaptureCameraCaptureSession: CameraCaptureSession? = null
    private var isVideoRecording: Boolean = false

    private fun startVideoCapture() {
        stopVideoPreview()

        val cameraDevice = cameraDevice ?: return

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
                val captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD)
                captureRequestBuilder.addTarget(surfaceTextureSurface)
                captureRequestBuilder.addTarget(mediaRecorderSurface)
                val request = captureRequestBuilder.build()
                session.setRepeatingRequest(request, null, null)
                videoCaptureCameraCaptureSession = session
                mediaRecorder.start()
                isVideoRecording = true

                ProfileHelper.isCameraProfileInvalidating = false
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

        mediaRecorder?.run {
            mediaRecorder = null
            try {
                stop()
            } catch (e: Exception) {
            }
            try {
                release()
            } catch (e: Exception) {
            }
        }

        videoFile?.run {
            videoFile = null
            toastFile(this)
        }

        videoCaptureCameraCaptureSession?.run {
            videoCaptureCameraCaptureSession = null
            try {
                stopRepeating()
            } catch (e: Exception) {
            }
            try {
                close()
            } catch (e: Exception) {
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
        AppHelper.toast(callback.getContext(), string)
        WindowCameraService.stop(callback.getContext())
    }
}
