package com.ebnbin.windowcamera.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.SurfaceTexture
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.Toast
import com.ebnbin.eb.util.AppHelper
import com.ebnbin.eb.util.BuildHelper
import com.ebnbin.eb.util.IntentHelper
import com.ebnbin.eb.util.SystemServices
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

    @SuppressLint("ShowToast")
    override fun windowToast(any: Any?, duration: Int, forceSystemAlertWindow: Boolean) {
        when (if (forceSystemAlertWindow) "system_alert_window" else ProfileHelper.toast.value) {
            "system_alert_window" -> {
                val toast = if (any is Int) {
                    Toast.makeText(context, any, Toast.LENGTH_SHORT)
                } else {
                    Toast.makeText(context, any.toString(), Toast.LENGTH_SHORT)
                }
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
                SystemServices.windowManager.addView(view, params)
                val delay = when (duration) {
                    Toast.LENGTH_SHORT -> 3000L
                    Toast.LENGTH_LONG -> 6000L
                    else -> duration.toLong()
                }
                view.postDelayed({
                    try {
                        SystemServices.windowManager.removeView(view)
                    } catch (e: Exception) {
                        // Ignore.
                    }
                }, delay)
            }
            "system" -> {
                if (duration != Toast.LENGTH_SHORT && duration != Toast.LENGTH_LONG) throw RuntimeException()
                if (any is Int) {
                    Toast.makeText(context, any, duration).show()
                } else {
                    Toast.makeText(context, any.toString(), duration).show()
                }
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
