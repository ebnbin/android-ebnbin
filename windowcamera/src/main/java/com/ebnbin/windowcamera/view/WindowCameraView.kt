package com.ebnbin.windowcamera.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.SurfaceTexture
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.Toast
import com.ebnbin.eb.util.AppHelper
import com.ebnbin.eb.util.BuildHelper
import com.ebnbin.eb.util.IntentHelper
import com.ebnbin.eb.util.ResHelper
import com.ebnbin.eb.util.SystemServices
import com.ebnbin.eb.util.dpToPxRound
import com.ebnbin.windowcamera.profile.ProfileHelper
import com.ebnbin.windowcamera.service.WindowCameraService
import com.ebnbin.windowcamera.view.camera.IWindowCameraViewCameraCallback
import com.ebnbin.windowcamera.view.camera.IWindowCameraViewCameraDelegate
import com.ebnbin.windowcamera.view.camera.WindowCameraViewCameraDelegate
import com.ebnbin.windowcamera.view.canvas.IWindowCameraViewCanvasCallback
import com.ebnbin.windowcamera.view.canvas.IWindowCameraViewCanvasDelegate
import com.ebnbin.windowcamera.view.canvas.WindowCameraViewCanvasDelegate
import com.ebnbin.windowcamera.view.gesture.IWindowCameraViewGestureCallback
import com.ebnbin.windowcamera.view.gesture.IWindowCameraViewGestureDelegate
import com.ebnbin.windowcamera.view.gesture.WindowCameraViewGestureDelegate
import com.ebnbin.windowcamera.view.layout.IWindowCameraViewLayoutCallback
import com.ebnbin.windowcamera.view.layout.IWindowCameraViewLayoutDelegate
import com.ebnbin.windowcamera.view.layout.WindowCameraViewLayoutDelegate
import com.ebnbin.windowcamera.view.surfacetexture.IWindowCameraViewSurfaceCallback
import com.ebnbin.windowcamera.view.surfacetexture.IWindowCameraViewSurfaceDelegate
import com.ebnbin.windowcamera.view.surfacetexture.WindowCameraViewSurfaceDelegate

/**
 * 用于 WindowCameraService 添加到 WindowManager 上的 view.
 *
 * TextureView 不支持 onDraw 或 onDrawForeground, 使用 FrameLayout 包装, 在 onDrawForeground 绘制自定义内容.
 */
class WindowCameraView(context: Context) : FrameLayout(context),
    IWindowCameraViewCameraCallback,
    IWindowCameraViewLayoutCallback,
    IWindowCameraViewSurfaceCallback,
    IWindowCameraViewGestureCallback,
    IWindowCameraViewCanvasCallback
{
    init {
        setWillNotDraw(false)
    }

    //*****************************************************************************************************************

    private var lastToastView: View? = null

    @SuppressLint("ShowToast")
    override fun toast(any: Any?, long: Boolean) {
        when (ProfileHelper.toast.value) {
            "system_alert_window" -> {
                lastToastView?.run {
                    val runnable = tag as Runnable
                    removeCallbacks(runnable)
                    runnable.run()
                }
                val toast = Toast.makeText(context, ResHelper.getString(any), Toast.LENGTH_SHORT)
                val view = toast.view
                val params = WindowManager.LayoutParams()
                params.width = ViewGroup.LayoutParams.WRAP_CONTENT
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT
                params.type = if (BuildHelper.sdk26O()) {
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                } else {
                    @Suppress("DEPRECATION")
                    WindowManager.LayoutParams.TYPE_PHONE
                }
                params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                params.format = PixelFormat.TRANSLUCENT
                params.windowAnimations = android.R.style.Animation_Toast
                params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
                params.y = 24f.dpToPxRound
                SystemServices.windowManager.addView(view, params)
                lastToastView = view
                val delay = if (long) 5000L else 2000L
                val runnable = Runnable {
                    lastToastView = null
                    try {
                        SystemServices.windowManager.removeView(view)
                    } catch (e: Exception) {
                        // Ignore.
                    }
                }
                view.tag = runnable
                view.postDelayed(runnable, delay)
            }
            "system" -> {
                AppHelper.toast(context, any, long)
            }
            "none" -> {
                // Do nothing.
            }
            else -> {
                throw RuntimeException()
            }
        }
    }

    //*****************************************************************************************************************

    private val cameraDelegate: IWindowCameraViewCameraDelegate = WindowCameraViewCameraDelegate(this)

    override fun getSurfaceTexture(): SurfaceTexture {
        return surfaceDelegate.getSurfaceTexture()
    }

    override fun invalidateSizePosition() {
        layoutDelegate.invalidateSizePosition()
    }

    //*****************************************************************************************************************

    private val layoutDelegate: IWindowCameraViewLayoutDelegate = WindowCameraViewLayoutDelegate(this)

    override fun updateLayoutParams(block: WindowManager.LayoutParams.() -> Unit) {
        val params = layoutParams as WindowManager.LayoutParams
        block(params)
        SystemServices.windowManager.updateViewLayout(this, params)
    }

    //*****************************************************************************************************************

    private val surfaceDelegate: IWindowCameraViewSurfaceDelegate = WindowCameraViewSurfaceDelegate(this)

    override fun getViewGroup(): ViewGroup {
        return this
    }

    override fun onSurfaceTextureCreated() {
        cameraDelegate.openCamera()
    }

    override fun onSurfaceTextureDestroyed() {
        cameraDelegate.closeCamera()
    }

    //*****************************************************************************************************************

    private val gestureDelegate: IWindowCameraViewGestureDelegate = WindowCameraViewGestureDelegate(this)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (gestureDelegate.onTouchEvent(event)) return true
        return super.onTouchEvent(event)
    }

    override fun getLayoutX(): Int {
        return (layoutParams as WindowManager.LayoutParams).x
    }

    override fun getLayoutY(): Int {
        return (layoutParams as WindowManager.LayoutParams).y
    }

    override fun onMove(layoutX: Int, layoutY: Int) {
        layoutDelegate.putPosition(layoutParams.width, layoutParams.height, layoutX, layoutY)
    }

    override fun onSingleTap() {
        cameraDelegate.capture()
    }

    override fun onDoubleTap() {
        IntentHelper.restartApp()
    }

    override fun onLongPress() {
        AppHelper.vibrate(100L)
        WindowCameraService.stop(context)
    }

    //*****************************************************************************************************************

    private val canvasDelegate: IWindowCameraViewCanvasDelegate = WindowCameraViewCanvasDelegate(this)

    override fun onDrawForeground(canvas: Canvas?) {
        super.onDrawForeground(canvas)
        canvasDelegate.onDraw(canvas)
    }

    //*****************************************************************************************************************

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        cameraDelegate.init()
        layoutDelegate.init()
        surfaceDelegate.init()
        gestureDelegate.init()
        canvasDelegate.init()
    }

    override fun onDetachedFromWindow() {
        canvasDelegate.dispose()
        gestureDelegate.dispose()
        surfaceDelegate.dispose()
        layoutDelegate.dispose()
        cameraDelegate.dispose()
        super.onDetachedFromWindow()
    }
}
