package com.ebnbin.windowcamera.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.media.ImageReader
import android.media.MediaRecorder
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.Surface
import android.view.TextureView
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.view.GestureDetectorCompat
import com.ebnbin.eb.sharedpreferences.getSharedPreferences
import com.ebnbin.eb.util.Ratio
import com.ebnbin.eb.util.RotationDetector
import com.ebnbin.eb.util.RotationSize
import com.ebnbin.eb.util.cameraManager
import com.ebnbin.eb.util.displayRealSize
import com.ebnbin.eb.util.displaySize
import com.ebnbin.eb.util.dpToPx
import com.ebnbin.eb.util.restartMainActivity
import com.ebnbin.eb.util.toast
import com.ebnbin.eb.util.vibrate
import com.ebnbin.eb.util.windowManager
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.camera.CameraHelper
import com.ebnbin.windowcamera.profile.ProfileHelper
import com.ebnbin.windowcamera.service.WindowCameraService
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * 用于 WindowCameraService 添加到 WindowManager 上的 view.
 *
 * TextureView 不支持 onDraw 或 onDrawForeground, 使用 FrameLayout 包装, 在 onDrawForeground 绘制自定义内容.
 */
class WindowCameraView(context: Context) : FrameLayout(context),
    TextureView.SurfaceTextureListener,
    SharedPreferences.OnSharedPreferenceChangeListener,
    RotationDetector.Listener,
    GestureDetector.OnGestureListener,
    GestureDetector.OnDoubleTapListener,
    ImageReader.OnImageAvailableListener
{
    private val textureView: TextureView = TextureView(this.context)

    init {
        setWillNotDraw(false)

        textureView.surfaceTextureListener = this
        addView(textureView)
    }

    //*****************************************************************************************************************

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        getSharedPreferences(ProfileHelper.sharedPreferencesName).registerOnSharedPreferenceChangeListener(this)
        RotationDetector.register(this)

        startBackgroundThread()

        displayRotation = com.ebnbin.eb.util.displayRotation

        invalidateLayout(invalidateIsOutEnabled = true, invalidateSize = true)
        invalidateAlpha()
        invalidateIsTouchable()
        invalidateCamera()
    }

    override fun onDetachedFromWindow() {
        stopBackgroundThread()

        RotationDetector.unregister(this)
        getSharedPreferences(ProfileHelper.sharedPreferencesName).unregisterOnSharedPreferenceChangeListener(this)
        super.onDetachedFromWindow()
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

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
        invalidateTransform()
        openCamera()
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
        invalidateTransform()
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        closeCamera()
        return true
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
    }

    //*****************************************************************************************************************

    private fun invalidateTransform() {
        textureView.surfaceTexture?.setDefaultBufferSize(previewResolution.width, previewResolution.height)

        val viewWidth = textureView.width.toFloat()
        val viewHeight = textureView.height.toFloat()

        val viewCenterX = 0.5f * viewWidth
        val viewCenterY = 0.5f * viewHeight

        val bufferWidth = previewResolution.width0.toFloat()
        val bufferHeight = previewResolution.height0.toFloat()

        val viewRectF = RectF(0f, 0f, viewWidth, viewHeight)

        val bufferLeft = 0.5f * (viewWidth - bufferWidth)
        val bufferTop = 0.5f * (viewHeight - bufferHeight)
        val bufferRight = bufferLeft + bufferWidth
        val bufferBottom = bufferTop + bufferHeight
        val bufferRectF = RectF(bufferLeft, bufferTop, bufferRight, bufferBottom)

        val matrix = Matrix()

        matrix.setRectToRect(viewRectF, bufferRectF, Matrix.ScaleToFit.FILL)

        val scaleX = viewWidth / previewResolution.width(displayRotation)
        val scaleY = viewHeight / previewResolution.height(displayRotation)
        val scale = max(scaleX, scaleY)
        matrix.postScale(scale, scale, viewCenterX, viewCenterY)

        val rotate = 360f - 90f * displayRotation
        matrix.postRotate(rotate, viewCenterX, viewCenterY)

        textureView.setTransform(matrix)
    }

    //*****************************************************************************************************************

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            ProfileHelper.KEY_SIZE -> {
                invalidateLayout(invalidateIsOutEnabled = false, invalidateSize = true)
            }
            ProfileHelper.KEY_RATIO -> {
                invalidateLayout(invalidateIsOutEnabled = false, invalidateSize = true)
            }
            ProfileHelper.KEY_X -> {
                invalidateLayout(invalidateIsOutEnabled = false, invalidateSize = false)
            }
            ProfileHelper.KEY_Y -> {
                invalidateLayout(invalidateIsOutEnabled = false, invalidateSize = false)
            }
            ProfileHelper.KEY_IS_OUT_ENABLED -> {
                invalidateLayout(invalidateIsOutEnabled = true, invalidateSize = true)
            }
            ProfileHelper.KEY_ALPHA -> {
                invalidateAlpha()
            }
            ProfileHelper.KEY_IS_TOUCHABLE -> {
                invalidateIsTouchable()
            }
            ProfileHelper.KEY_IS_FRONT -> {
                closeCamera()
                invalidateCamera()
                openCamera()
            }
            ProfileHelper.KEY_IS_VIDEO -> {
                closeCamera()
                invalidateCamera()
                openCamera()
            }
            ProfileHelper.KEY_BACK_PHOTO_RESOLUTION -> {
                closeCamera()
                invalidateCamera()
                openCamera()
            }
            ProfileHelper.KEY_BACK_VIDEO_PROFILE -> {
                closeCamera()
                invalidateCamera()
                openCamera()
            }
            ProfileHelper.KEY_FRONT_PHOTO_RESOLUTION -> {
                closeCamera()
                invalidateCamera()
                openCamera()
            }
            ProfileHelper.KEY_FRONT_VIDEO_PROFILE -> {
                closeCamera()
                invalidateCamera()
                openCamera()
            }
        }
    }

    //*****************************************************************************************************************

    private var displayRotation: Int = 0

    override fun onRotationChanged(oldRotation: Int, newRotation: Int) {
        displayRotation = newRotation
        invalidateLayout(invalidateIsOutEnabled = false, invalidateSize = true)
        invalidateTransform()
    }

    //*****************************************************************************************************************

    private fun updateLayoutParams(block: WindowManager.LayoutParams.() -> Unit) {
        val params = layoutParams as WindowManager.LayoutParams
        block(params)
        windowManager.updateViewLayout(this, params)
    }

    private fun invalidateLayout(invalidateIsOutEnabled: Boolean, invalidateSize: Boolean) {
        updateLayoutParams {
            // 参数不合法.
            if (invalidateIsOutEnabled && !invalidateSize) throw RuntimeException()

            fun calcPosition(range: Int, percent: Int, offset: Int): Int {
                return (range * percent / 100f + offset).roundToInt()
            }

            val isOutEnabled = ProfileHelper.isOutEnabled
            if (invalidateIsOutEnabled) {
                flags = if (isOutEnabled) {
                    flags or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                } else {
                    flags or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS xor
                            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                }
            }
            val displaySize = if (isOutEnabled) displayRealSize else displaySize
            val rotationSize: RotationSize
            if (invalidateIsOutEnabled || invalidateSize) {
                val sizeSp = ProfileHelper.size
                val ratioSp = ProfileHelper.ratio
                val ratio = when (ratioSp) {
                    "capture" -> {
                        // TODO
                        ProfileHelper.device().maxResolution.ratio
                    }
                    "raw" -> ProfileHelper.device().maxResolution.ratio
                    "screen" -> displaySize.ratio
                    "square" -> Ratio.SQUARE
                    else -> throw RuntimeException()
                }
                rotationSize = displaySize.crop(ratio, sizeSp)
                width = rotationSize.width
                height = rotationSize.height
            } else {
                rotationSize = RotationSize(width, height, displaySize.rotation)
            }
            val xSp = ProfileHelper.x
            val xRange: Int
            val xPercent: Int
            val xOffset: Int
            when (xSp) {
                in 0..100 -> {
                    xRange = displaySize.width - rotationSize.width
                    xPercent = xSp
                    xOffset = 0
                }
                in -99..-1 -> {
                    xRange = rotationSize.width
                    xPercent = xSp + 100
                    xOffset = -rotationSize.width
                }
                in 101..199 -> {
                    xRange = rotationSize.width
                    xPercent = xSp - 100
                    xOffset = displaySize.width - rotationSize.width
                }
                else -> throw RuntimeException()
            }
            x = calcPosition(xRange, xPercent, xOffset)
            val ySp = ProfileHelper.y
            val yRange: Int
            val yPercent: Int
            val yOffset: Int
            when (ySp) {
                in 0..100 -> {
                    yRange = displaySize.height - rotationSize.height
                    yPercent = ySp
                    yOffset = 0
                }
                in -99..-1 -> {
                    yRange = rotationSize.height
                    yPercent = ySp + 100
                    yOffset = -rotationSize.height
                }
                in 101..199 -> {
                    yRange = rotationSize.height
                    yPercent = ySp - 100
                    yOffset = displaySize.height - rotationSize.height
                }
                else -> throw RuntimeException()
            }
            y = calcPosition(yRange, yPercent, yOffset)
        }
    }

    private fun invalidateAlpha() {
        updateLayoutParams {
            alpha = ProfileHelper.alpha / 100f
        }
    }

    private fun invalidateIsTouchable() {
        updateLayoutParams {
            flags = if (ProfileHelper.isTouchable) {
                flags or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE xor
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            } else {
                flags or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            }
        }
    }

    //*****************************************************************************************************************

    private val gestureDetector: GestureDetectorCompat = GestureDetectorCompat(context, this)

    private var downX: Int = 0
    private var downY: Int = 0
    private var downRawX: Float = 0f
    private var downRawY: Float = 0f

    private var longPressed: Boolean = false

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return super.onTouchEvent(event)

        if (gestureDetector.onTouchEvent(event)) return true

        if (event.actionMasked == MotionEvent.ACTION_UP) {
            if (longPressed) {
                longPressed = false
            } else {
//                putPosition(event)
            }
        }

        return super.onTouchEvent(event)
    }

    private fun putPosition(motionEvent: MotionEvent) {
        fun calcPositionPercent(position: Float, range: Int, percentOffset: Int, isOutEnabled: Boolean): Int {
            var positionPercent = (position / range * 100).toInt() + percentOffset
            positionPercent = min(positionPercent, if (isOutEnabled) 199 else 100)
            positionPercent = max(positionPercent, if (isOutEnabled) -99 else 0)
            return positionPercent
        }

        val x = downX + motionEvent.rawX - downRawX
        val y = downY + motionEvent.rawY - downRawY

        val isOutEnabled = ProfileHelper.isOutEnabled
        val displaySize = if (isOutEnabled) displayRealSize else displaySize

        val xMin = 0
        val xMax = displaySize.width - layoutParams.width
        val xPosition: Float
        val xRange: Int
        val xPercentOffset: Int
        when {
            x in xMin.toFloat()..xMax.toFloat() -> {
                xPosition = x
                xRange = xMax - xMin
                xPercentOffset = 0
            }
            x < xMin -> {
                xPosition = x + layoutParams.width
                xRange = layoutParams.width
                xPercentOffset = -100
            }
            else -> {
                xPosition = x + layoutParams.width - displaySize.width
                xRange = layoutParams.width
                xPercentOffset = 100
            }
        }
        val xPercent = calcPositionPercent(xPosition, xRange, xPercentOffset, isOutEnabled)

        val yMin = 0
        val yMax = displaySize.height - layoutParams.height
        val yPosition: Float
        val yRange: Int
        val yPercentOffset: Int
        when {
            y in yMin.toFloat()..yMax.toFloat() -> {
                yPosition = y
                yRange = yMax - yMin
                yPercentOffset = 0
            }
            y < yMin -> {
                yPosition = y + layoutParams.height
                yRange = layoutParams.height
                yPercentOffset = -100
            }
            else -> {
                yPosition = y + layoutParams.height - displaySize.height
                yRange = layoutParams.height
                yPercentOffset = 100
            }
        }
        val yPercent = calcPositionPercent(yPosition, yRange, yPercentOffset, isOutEnabled)

        val xSp = ProfileHelper.x
        val ySp = ProfileHelper.y
        if (xPercent == xSp && yPercent == ySp) {
            invalidateLayout(invalidateIsOutEnabled = false, invalidateSize = false)
        } else {
            ProfileHelper.x = xPercent
            ProfileHelper.y = yPercent
        }
    }

    override fun onDown(e: MotionEvent?): Boolean {
        e ?: return false

        val params = layoutParams as WindowManager.LayoutParams
        downX = params.x
        downY = params.y
        downRawX = e.rawX
        downRawY = e.rawY
        return false
    }

    override fun onShowPress(e: MotionEvent?) {
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return false
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        e2 ?: return false
        putPosition(e2)
        return false
    }

    override fun onLongPress(e: MotionEvent?) {
        longPressed = true
        vibrate(100L)
        WindowCameraService.stop(context)
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        return false
    }

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        if (isVideo) {
            videoCapture()
        } else {
            photoCapture()
        }
        return false
    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        vibrate(50L)
        restartMainActivity()
        return false
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        return false
    }

    //*****************************************************************************************************************

    private lateinit var device: CameraHelper.Device
    private var isVideo: Boolean = false
    private lateinit var photoResolution: CameraHelper.Device.Resolution
    private lateinit var videoProfile: CameraHelper.Device.VideoProfile
    private lateinit var previewResolution: CameraHelper.Device.Resolution

    private fun invalidateCamera() {
        device = ProfileHelper.device()
        isVideo = ProfileHelper.isVideo
        val photoResolution = ProfileHelper.photoResolution()
        if (photoResolution != null) {
            this.photoResolution = photoResolution
        }
        val videoProfile = ProfileHelper.videoProfile()
        if (videoProfile != null) {
            this.videoProfile = videoProfile
        }
        previewResolution = device.previewResolutions.first()

        invalidateTransform()
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

        val surfaceTextureSurface = Surface(textureView.surfaceTexture)
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
        val request = captureRequestBuilder.build()
        photoCameraCaptureSession.capture(request, null, null)
    }

    override fun onImageAvailable(reader: ImageReader?) {
        reader ?: return
        val image = reader.acquireNextImage() ?: return

        val byteBuffer = image.planes[0].buffer
        val byteArray = ByteArray(byteBuffer.remaining())
        byteBuffer.get(byteArray)
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DCIM), "${System.currentTimeMillis()}.jpg")
        val fos = FileOutputStream(file)
        fos.write(byteArray)
        fos.close()
        image.close()

        toast(context, file)
    }

    //*****************************************************************************************************************

    private var videoCameraCaptureSession: CameraCaptureSession? = null

    private var videoFile: File? = null

    private var mediaRecorder: MediaRecorder? = null

    private var isVideoRecording: Boolean = false

    private fun startVideoPreview() {
        val cameraDevice = cameraDevice ?: return

        val surfaceTextureSurface = Surface(textureView.surfaceTexture)
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

        val videoFile = File(context.getExternalFilesDir(Environment.DIRECTORY_DCIM),
            "${System.currentTimeMillis()}.mp4")
        val mediaRecorder = MediaRecorder()
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE)
        mediaRecorder.setProfile(videoProfile.camcorderProfile)
        mediaRecorder.setOutputFile(videoFile.absolutePath)
        mediaRecorder.prepare()
        this.videoFile = videoFile
        this.mediaRecorder = mediaRecorder

        val surfaceTextureSurface = Surface(textureView.surfaceTexture)
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
            toast(context, this)
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

    private fun onCameraError() {
        toast(context, R.string.camera_error)
        WindowCameraService.stop(context)
    }

    //*****************************************************************************************************************

    private val paint: Paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 1.6f.dpToPx
        color = Color.RED
    }

    override fun onDrawForeground(canvas: Canvas?) {
        super.onDrawForeground(canvas)
        canvas ?: return
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }
}
