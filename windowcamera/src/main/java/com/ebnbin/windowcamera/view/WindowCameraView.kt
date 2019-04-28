package com.ebnbin.windowcamera.view

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
import com.ebnbin.windowcamera.service.WindowCameraService

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

        cameraDelegate.invalidateCamera()
        layoutDelegate.invalidateLayout(invalidateIsOutEnabled = true, invalidateSize = true)
        layoutDelegate.invalidateAlpha()
        layoutDelegate.invalidateIsKeepScreenOnEnabled()
        layoutDelegate.invalidateIsTouchable()
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
        cameraDelegate.invalidateTransform()
        cameraDelegate.openCamera()
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
        cameraDelegate.invalidateTransform()
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        cameraDelegate.closeCamera()
        return true
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
    }

    //*****************************************************************************************************************

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            ProfileHelper.size.key -> {
                layoutDelegate.invalidateLayout(invalidateIsOutEnabled = false, invalidateSize = true)
            }
            ProfileHelper.ratio.key -> {
                layoutDelegate.invalidateLayout(invalidateIsOutEnabled = false, invalidateSize = true)
            }
            ProfileHelper.is_out_enabled.key -> {
                layoutDelegate.invalidateLayout(invalidateIsOutEnabled = true, invalidateSize = true)
            }
            ProfileHelper.in_x.key -> {
                layoutDelegate.invalidateLayout(invalidateIsOutEnabled = false, invalidateSize = false)
            }
            ProfileHelper.in_y.key -> {
                layoutDelegate.invalidateLayout(invalidateIsOutEnabled = false, invalidateSize = false)
            }
            ProfileHelper.out_x.key -> {
                layoutDelegate.invalidateLayout(invalidateIsOutEnabled = false, invalidateSize = false)
            }
            ProfileHelper.out_y.key -> {
                layoutDelegate.invalidateLayout(invalidateIsOutEnabled = false, invalidateSize = false)
            }
            ProfileHelper.alpha.key -> {
                layoutDelegate.invalidateAlpha()
            }
            ProfileHelper.is_keep_screen_on_enabled.key -> {
                layoutDelegate.invalidateIsKeepScreenOnEnabled()
            }
            ProfileHelper.is_touchable.key -> {
                layoutDelegate.invalidateIsTouchable()
            }
            ProfileHelper.is_move_enabled.key -> {
                gestureDelegate.isMoveEnabled = ProfileHelper.is_move_enabled.value
            }
            ProfileHelper.is_front.key -> {
                cameraDelegate.reopenCamera()
            }
            ProfileHelper.is_video.key -> {
                cameraDelegate.reopenCamera()
            }
            ProfileHelper.back_photo_resolution.key -> {
                cameraDelegate.reopenCamera(true)
            }
            ProfileHelper.back_video_profile.key -> {
                cameraDelegate.reopenCamera(true)
            }
            ProfileHelper.front_photo_resolution.key -> {
                cameraDelegate.reopenCamera(true)
            }
            ProfileHelper.front_video_profile.key -> {
                cameraDelegate.reopenCamera(true)
            }
        }
    }

    //*****************************************************************************************************************

    override fun onRotationChanged(oldRotation: Int, newRotation: Int) {
        cameraDelegate.displayRotation = newRotation
        layoutDelegate.invalidateLayout(invalidateIsOutEnabled = false, invalidateSize = true)
        cameraDelegate.invalidateTransform()
    }

    //*****************************************************************************************************************

    val layoutDelegate: WindowCameraViewLayoutDelegate =
        WindowCameraViewLayoutDelegate(this)

    fun getRatioBySp(): Ratio {
        return cameraDelegate.getRatioBySp()
    }

    fun updateLayoutParams(block: WindowManager.LayoutParams.() -> Unit) {
        val params = layoutParams as WindowManager.LayoutParams
        block(params)
        SystemServices.windowManager.updateViewLayout(this, params)
    }

    //*****************************************************************************************************************

    private val gestureDelegate: WindowCameraViewGestureDelegate =
        WindowCameraViewGestureDelegate(this)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (gestureDelegate.onTouchEvent(event)) return true
        return super.onTouchEvent(event)
    }

    fun onMove(x: Float, y: Float) {
        layoutDelegate.putPosition(layoutParams.width, layoutParams.height, x, y)
    }

    fun onSingleTap() {
        cameraDelegate.onSingleTap()
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

    private val cameraDelegate: WindowCameraViewCameraDelegate =
        WindowCameraViewCameraDelegate(this)

    fun getSurfaceTexture(): SurfaceTexture {
        return textureView.surfaceTexture
    }

    //*****************************************************************************************************************

    private val canvasDelegate: WindowCameraViewCanvasDelegate =
        WindowCameraViewCanvasDelegate(this)

    override fun onDrawForeground(canvas: Canvas?) {
        super.onDrawForeground(canvas)
        canvasDelegate.onDraw(canvas)
    }
}
