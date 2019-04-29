package com.ebnbin.windowcamera.view

import android.annotation.SuppressLint
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
import com.ebnbin.eb.util.TimeHelper
import com.ebnbin.eb.util.WindowHelper
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.camera.CameraHelper
import com.ebnbin.windowcamera.profile.ProfileHelper
import com.ebnbin.windowcamera.service.WindowCameraService
import com.ebnbin.windowcamera.util.IOHelper
import java.io.File
import java.io.FileOutputStream

class WindowCameraViewCameraDelegate(private val windowCameraView: WindowCameraView) :
    ImageReader.OnImageAvailableListener
{
    fun init() {
        startBackgroundThread()
        invalidateCamera()
    }

    fun dispose() {
        stopBackgroundThread()
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

    private lateinit var device: CameraHelper.Device
    private var isVideo: Boolean = false
    private lateinit var photoResolution: CameraHelper.Device.Resolution
    private lateinit var videoProfile: CameraHelper.Device.VideoProfile
    lateinit var previewResolution: CameraHelper.Device.Resolution

    private fun invalidateCamera() {
        device = ProfileHelper.device()
        isVideo = ProfileHelper.is_video.value
        if (isVideo) {
            videoProfile = ProfileHelper.videoProfile()
        } else {
            photoResolution = ProfileHelper.photoResolution()
        }
        previewResolution = device.defaultPreviewResolution

//        invalidateTransform()
    }

    //*****************************************************************************************************************

    private var cameraDevice: CameraDevice? = null

    @SuppressLint("MissingPermission")
    fun openCamera() {
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
        SystemServices.cameraManager.openCamera(device.id, callback, null)
    }

    fun closeCamera() {
        ProfileHelper.isCameraProfileInvalidating = true

        stopPreview()

        cameraDevice?.run {
            cameraDevice = null
            close()
        }

        ProfileHelper.isCameraProfileInvalidating = false
    }

    //*****************************************************************************************************************

    private fun startPreview() {
        if (isVideo) {
            startVideoPreview()
        } else {
            startPhotoPreview()
        }
    }

    private fun stopPreview() {
        if (isVideo) {
            stopVideoCapture(false)
            stopVideoPreview()
        } else {
            stopPhotoPreview()
        }
    }

    //*****************************************************************************************************************

    private var imageReader: ImageReader? = null

    private var photoCameraCaptureSession: CameraCaptureSession? = null

    private fun startPhotoPreview() {
        val cameraDevice = cameraDevice ?: return

        val imageReader = ImageReader.newInstance(photoResolution.width, photoResolution.height, ImageFormat.JPEG, 2)
        imageReader.setOnImageAvailableListener(this, null)
        this.imageReader = imageReader

        val surfaceTextureSurface = Surface(windowCameraView.getSurfaceTexture())
        val imageReaderSurface = imageReader.surface
        val outputs = listOf(surfaceTextureSurface, imageReaderSurface)
        val callback = object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                photoCameraCaptureSession = session

                val captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                captureRequestBuilder.addTarget(surfaceTextureSurface)
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

    private fun stopPhotoPreview() {
        photoCameraCaptureSession?.run {
            photoCameraCaptureSession = null
            try {
                stopRepeating()
            } catch (e: IllegalStateException) {
                // stopRepeating 对应 setRepeatingRequest, 但有可能出现 photoCameraCaptureSession 已经 closed 的情况.
            }
            close()
        }

        imageReader?.run {
            imageReader = null
            close()
        }
    }

    private fun photoCapture() {
        val photoCameraCaptureSession = photoCameraCaptureSession ?: return
        val cameraDevice = cameraDevice ?: return
        val imageReader = imageReader ?: return

        val imageReaderSurface = imageReader.surface
        val captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
        captureRequestBuilder.addTarget(imageReaderSurface)
        captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION,
            device.sensorOrientations.getValue(WindowHelper.displayRotation))
        val request = captureRequestBuilder.build()
        photoCameraCaptureSession.capture(request, null, null)
    }

    override fun onImageAvailable(reader: ImageReader?) {
        reader ?: return
        val image = reader.acquireNextImage() ?: return

        val byteBuffer = image.planes[0].buffer
        val byteArray = ByteArray(byteBuffer.remaining())
        byteBuffer.get(byteArray)
        val file = nextFile(".jpg")
        val fos = FileOutputStream(file)
        fos.write(byteArray)
        fos.close()
        image.close()

        AppHelper.toast(windowCameraView.context, file)
    }

    //*****************************************************************************************************************

    private var videoCameraCaptureSession: CameraCaptureSession? = null

    private var videoFile: File? = null

    private var mediaRecorder: MediaRecorder? = null

    private var isVideoRecording: Boolean = false

    private fun startVideoPreview() {
        val cameraDevice = cameraDevice ?: return

        val surfaceTextureSurface = Surface(windowCameraView.getSurfaceTexture())
        val outputs = listOf(surfaceTextureSurface)
        val callback = object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                videoCameraCaptureSession = session

                val captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                captureRequestBuilder.addTarget(surfaceTextureSurface)
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

    private fun stopVideoPreview() {
        videoCameraCaptureSession?.run {
            videoCameraCaptureSession = null
            try {
                stopRepeating()
            } catch (e: IllegalStateException) {
                // stopRepeating 对应 setRepeatingRequest, 但有可能出现 videoCameraCaptureSession 已经 closed 的情况.
            }
            close()
        }
    }

    private fun startVideoCapture() {
        stopVideoPreview()

        val cameraDevice = cameraDevice ?: return

        val videoFile = nextFile(videoProfile.extension)
        val mediaRecorder = MediaRecorder()
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE)
        mediaRecorder.setProfile(videoProfile.camcorderProfile)
        mediaRecorder.setOutputFile(videoFile.absolutePath)
        mediaRecorder.setOrientationHint(device.sensorOrientations.getValue(WindowHelper.displayRotation))
        mediaRecorder.prepare()
        this.videoFile = videoFile
        this.mediaRecorder = mediaRecorder

        val surfaceTextureSurface = Surface(windowCameraView.getSurfaceTexture())
        val mediaRecorderSurface = mediaRecorder.surface
        val outputs = listOf(surfaceTextureSurface, mediaRecorderSurface)
        val callback = object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                videoCameraCaptureSession = session

                val captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD)
                captureRequestBuilder.addTarget(surfaceTextureSurface)
                captureRequestBuilder.addTarget(mediaRecorderSurface)
                val request = captureRequestBuilder.build()
                session.setRepeatingRequest(request, null, null)
                mediaRecorder.start()
                isVideoRecording = true

                ProfileHelper.isCameraProfileInvalidating = false
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {
                onCameraError()
            }
        }
        cameraDevice.createCaptureSession(outputs, callback, null)
    }

    private fun stopVideoCapture(resumePreview: Boolean) {
        if (!isVideoRecording) return
        isVideoRecording = false

        mediaRecorder?.run {
            mediaRecorder = null
            stop()
            release()
        }

        videoFile?.run {
            videoFile = null
            AppHelper.toast(windowCameraView.context, this)
        }

        videoCameraCaptureSession?.run {
            videoCameraCaptureSession = null
            try {
                stopRepeating()
            } catch (e: IllegalStateException) {
                // stopRepeating 对应 setRepeatingRequest, 但有可能出现 videoCameraCaptureSession 已经 closed 的情况.
            }
            close()
        }

        if (resumePreview) {
            startVideoPreview()
        }
    }

    private fun videoCapture() {
        if (isVideoRecording) {
            stopVideoCapture(true)
        } else {
            startVideoCapture()
        }
    }

    //*****************************************************************************************************************

    private fun nextFile(extension: String): File {
        val path = IOHelper.getPath()
        if (!path.exists()) {
            path.mkdirs()
        }
        val fileName = "${TimeHelper.string("yyyy_MM_dd_HH_mm_ss_SSS")}$extension"
        return File(path, fileName)
    }

    //*****************************************************************************************************************

    private fun onCameraError() {
        AppHelper.toast(windowCameraView.context, R.string.camera_error)
        WindowCameraService.stop(windowCameraView.context)
    }

    //*****************************************************************************************************************

    fun onSingleTap() {
        if (isVideo) {
            videoCapture()
        } else {
            photoCapture()
        }
    }

    fun getResolution(): CameraHelper.Device.Resolution {
        return if (isVideo) videoProfile else photoResolution
    }

    fun getMaxResolution(): CameraHelper.Device.Resolution {
        return device.maxResolution
    }

    fun reopenCamera() {
        closeCamera()
        invalidateCamera()
        windowCameraView.invalidateSizePosition()
        openCamera()
    }
}
