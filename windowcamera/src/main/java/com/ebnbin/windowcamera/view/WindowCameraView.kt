package com.ebnbin.windowcamera.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.SurfaceTexture
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import com.ebnbin.eb.util.AppHelper
import com.ebnbin.eb.util.Ratio
import com.ebnbin.eb.util.RotationSize
import com.ebnbin.eb.util.SystemServices
import com.ebnbin.windowcamera.service.WindowCameraService
import com.ebnbin.windowcamera.view.canvas.IWindowCameraViewCanvasDelegate
import com.ebnbin.windowcamera.view.canvas.WindowCameraViewCanvasCallback
import com.ebnbin.windowcamera.view.canvas.WindowCameraViewCanvasDelegate

/**
 * 用于 WindowCameraService 添加到 WindowManager 上的 view.
 *
 * TextureView 不支持 onDraw 或 onDrawForeground, 使用 FrameLayout 包装, 在 onDrawForeground 绘制自定义内容.
 */
class WindowCameraView(context: Context) : FrameLayout(context),
    WindowCameraViewCanvasCallback
{
    init {
        setWillNotDraw(false)
    }

    //*****************************************************************************************************************

    val surfaceDelegate: WindowCameraViewSurfaceDelegate = WindowCameraViewSurfaceDelegate(this)

    fun getViewGroup(): ViewGroup {
        return this
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

    val gestureDelegate: WindowCameraViewGestureDelegate = WindowCameraViewGestureDelegate(this)

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

    val cameraDelegate: WindowCameraViewCameraDelegate =
        WindowCameraViewCameraDelegate(this)

    fun getSurfaceTexture(): SurfaceTexture {
        return surfaceDelegate.textureView.surfaceTexture
    }

    fun getDisplaySize(): RotationSize {
        return layoutDelegate.displaySize
    }

    //*****************************************************************************************************************

    private val canvasDelegate: IWindowCameraViewCanvasDelegate = WindowCameraViewCanvasDelegate(this)

    override fun onDrawForeground(canvas: Canvas?) {
        super.onDrawForeground(canvas)
        canvasDelegate.onDraw(canvas)
    }

    //*****************************************************************************************************************

    val sharedPreferencesDelegate: WindowCameraViewSharedPreferencesDelegate =
        WindowCameraViewSharedPreferencesDelegate(this)

    //*****************************************************************************************************************

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        sharedPreferencesDelegate.init()
        cameraDelegate.init()
        layoutDelegate.init()
        canvasDelegate.init(this)
    }

    override fun onDetachedFromWindow() {
        canvasDelegate.dispose()
        layoutDelegate.dispose()
        cameraDelegate.dispose()
        sharedPreferencesDelegate.dispose()
        super.onDetachedFromWindow()
    }
}
