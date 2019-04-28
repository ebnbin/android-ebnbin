package com.ebnbin.windowcamera.app

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.SurfaceTexture
import android.os.Handler
import android.os.HandlerThread
import android.view.MotionEvent
import android.view.TextureView
import android.view.WindowManager
import android.widget.FrameLayout
import com.ebnbin.eb.sharedpreferences.SharedPreferencesHelper
import com.ebnbin.eb.util.AppHelper
import com.ebnbin.eb.util.Ratio
import com.ebnbin.eb.util.RotationDetector
import com.ebnbin.eb.util.SystemServices
import com.ebnbin.windowcamera.profile.ProfileHelper

/**
 * 用于 WindowCameraService 添加到 WindowManager 上的 view.
 *
 * TextureView 不支持 onDraw 或 onDrawForeground, 使用 FrameLayout 包装, 在 onDrawForeground 绘制自定义内容.
 */
class WindowCameraView(context: Context) : FrameLayout(context),
    TextureView.SurfaceTextureListener,
    SharedPreferences.OnSharedPreferenceChangeListener,
    RotationDetector.Listener
{
    val textureView: TextureView = TextureView(this.context)

    init {
        setWillNotDraw(false)

        textureView.surfaceTextureListener = this
        addView(textureView)
    }

    //*****************************************************************************************************************

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        SharedPreferencesHelper.get(ProfileHelper.SHARED_PREFERENCES_NAME_POSTFIX)
            .registerOnSharedPreferenceChangeListener(this)
        RotationDetector.register(this)

        startBackgroundThread()

        cameraHelper.invalidateCamera()
        layoutHelper.invalidateLayout(invalidateIsOutEnabled = true, invalidateSize = true)
        layoutHelper.invalidateAlpha()
        layoutHelper.invalidateIsKeepScreenOnEnabled()
        layoutHelper.invalidateIsTouchable()
    }

    override fun onDetachedFromWindow() {
        stopBackgroundThread()

        RotationDetector.unregister(this)
        SharedPreferencesHelper.get(ProfileHelper.SHARED_PREFERENCES_NAME_POSTFIX)
            .unregisterOnSharedPreferenceChangeListener(this)
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
        cameraHelper.invalidateTransform()
        cameraHelper.openCamera()
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
        cameraHelper.invalidateTransform()
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        cameraHelper.closeCamera()
        return true
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
    }

    //*****************************************************************************************************************

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            ProfileHelper.size.key -> {
                layoutHelper.invalidateLayout(invalidateIsOutEnabled = false, invalidateSize = true)
            }
            ProfileHelper.ratio.key -> {
                layoutHelper.invalidateLayout(invalidateIsOutEnabled = false, invalidateSize = true)
            }
            ProfileHelper.is_out_enabled.key -> {
                layoutHelper.invalidateLayout(invalidateIsOutEnabled = true, invalidateSize = true)
            }
            ProfileHelper.in_x.key -> {
                layoutHelper.invalidateLayout(invalidateIsOutEnabled = false, invalidateSize = false)
            }
            ProfileHelper.in_y.key -> {
                layoutHelper.invalidateLayout(invalidateIsOutEnabled = false, invalidateSize = false)
            }
            ProfileHelper.out_x.key -> {
                layoutHelper.invalidateLayout(invalidateIsOutEnabled = false, invalidateSize = false)
            }
            ProfileHelper.out_y.key -> {
                layoutHelper.invalidateLayout(invalidateIsOutEnabled = false, invalidateSize = false)
            }
            ProfileHelper.alpha.key -> {
                layoutHelper.invalidateAlpha()
            }
            ProfileHelper.is_keep_screen_on_enabled.key -> {
                layoutHelper.invalidateIsKeepScreenOnEnabled()
            }
            ProfileHelper.is_touchable.key -> {
                layoutHelper.invalidateIsTouchable()
            }
            ProfileHelper.is_move_enabled.key -> {
                gestureHelper.isMoveEnabled = ProfileHelper.is_move_enabled.value
            }
            ProfileHelper.is_front.key -> {
                cameraHelper.reopenCamera()
            }
            ProfileHelper.is_video.key -> {
                cameraHelper.reopenCamera()
            }
            ProfileHelper.back_photo_resolution.key -> {
                cameraHelper.reopenCamera(true)
            }
            ProfileHelper.back_video_profile.key -> {
                cameraHelper.reopenCamera(true)
            }
            ProfileHelper.front_photo_resolution.key -> {
                cameraHelper.reopenCamera(true)
            }
            ProfileHelper.front_video_profile.key -> {
                cameraHelper.reopenCamera(true)
            }
        }
    }

    //*****************************************************************************************************************

    override fun onRotationChanged(oldRotation: Int, newRotation: Int) {
        cameraHelper.displayRotation = newRotation
        layoutHelper.invalidateLayout(invalidateIsOutEnabled = false, invalidateSize = true)
        cameraHelper.invalidateTransform()
    }

    //*****************************************************************************************************************

    val layoutHelper: WindowCameraViewLayoutHelper = WindowCameraViewLayoutHelper(this)

    fun getRatioBySp(): Ratio {
        return cameraHelper.getRatioBySp()
    }

    fun updateLayoutParams(block: WindowManager.LayoutParams.() -> Unit) {
        val params = layoutParams as WindowManager.LayoutParams
        block(params)
        SystemServices.windowManager.updateViewLayout(this, params)
    }

    //*****************************************************************************************************************

    private val gestureHelper: WindowCameraViewGestureHelper = WindowCameraViewGestureHelper(this)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (gestureHelper.onTouchEvent(event)) return true
        return super.onTouchEvent(event)
    }

    fun onMove(x: Float, y: Float) {
        layoutHelper.putPosition(layoutParams.width, layoutParams.height, x, y)
    }

    fun onSingleTap() {
        cameraHelper.onSingleTap()
    }

    fun onDoubleTap() {
        AppHelper.vibrate(50L)
        AppHelper.restartMainActivity()
    }

    fun onLongPress() {
        AppHelper.vibrate(100L)
        WindowCameraService.stop(context)
    }

    //*****************************************************************************************************************

    private val cameraHelper: WindowCameraViewCameraHelper = WindowCameraViewCameraHelper(this)

    fun getSurfaceTexture(): SurfaceTexture {
        return textureView.surfaceTexture
    }

    //*****************************************************************************************************************

    private val canvasHelper: WindowCameraViewCanvasHelper = WindowCameraViewCanvasHelper(this)

    override fun onDrawForeground(canvas: Canvas?) {
        super.onDrawForeground(canvas)
        canvasHelper.onDraw(canvas)
    }
}
