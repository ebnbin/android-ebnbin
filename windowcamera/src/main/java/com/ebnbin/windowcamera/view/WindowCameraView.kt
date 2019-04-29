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
import com.ebnbin.eb.util.SystemServices
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
import com.ebnbin.windowcamera.view.surfacetexture.IWindowCameraViewSurfaceTextureCallback
import com.ebnbin.windowcamera.view.surfacetexture.IWindowCameraViewSurfaceTextureDelegate
import com.ebnbin.windowcamera.view.surfacetexture.WindowCameraViewSurfaceTextureDelegate

/**
 * 用于 WindowCameraService 添加到 WindowManager 上的 view.
 *
 * TextureView 不支持 onDraw 或 onDrawForeground, 使用 FrameLayout 包装, 在 onDrawForeground 绘制自定义内容.
 */
class WindowCameraView(context: Context) : FrameLayout(context),
    IWindowCameraViewCanvasCallback,
    IWindowCameraViewGestureCallback,
    IWindowCameraViewSurfaceTextureCallback,
    IWindowCameraViewLayoutCallback,
    IWindowCameraViewCameraCallback
{
    init {
        setWillNotDraw(false)
    }

    //*****************************************************************************************************************

    private val surfaceTextureDelegate: IWindowCameraViewSurfaceTextureDelegate =
        WindowCameraViewSurfaceTextureDelegate(this)

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

    private val layoutDelegate: IWindowCameraViewLayoutDelegate = WindowCameraViewLayoutDelegate(this)

    override fun updateLayoutParams(block: WindowManager.LayoutParams.() -> Unit) {
        val params = layoutParams as WindowManager.LayoutParams
        block(params)
        SystemServices.windowManager.updateViewLayout(this, params)
    }

    override fun invalidateSizePosition() {
        layoutDelegate.invalidateSizePosition()
    }

    //*****************************************************************************************************************

    private val gestureDelegate: IWindowCameraViewGestureDelegate = WindowCameraViewGestureDelegate(this)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (gestureDelegate.onTouchEvent(event)) return true
        return super.onTouchEvent(event)
    }

    override fun onMove(layoutX: Int, layoutY: Int) {
        layoutDelegate.putPosition(layoutParams.width, layoutParams.height, layoutX, layoutY)
    }

    override fun onSingleTap() {
        cameraDelegate.capture()
    }

    override fun onDoubleTap() {
        AppHelper.restartMainActivity()
    }

    override fun onLongPress() {
        AppHelper.vibrate(100L)
        WindowCameraService.stop(context)
    }

    override fun getLayoutX(): Int {
        return (layoutParams as WindowManager.LayoutParams).x
    }

    override fun getLayoutY(): Int {
        return (layoutParams as WindowManager.LayoutParams).y
    }

    //*****************************************************************************************************************

    private val cameraDelegate: IWindowCameraViewCameraDelegate = WindowCameraViewCameraDelegate(this)

    override fun getSurfaceTexture(): SurfaceTexture {
        return surfaceTextureDelegate.getSurfaceTexture()
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
        // TODO layout 要比 surfaceTexture 先初始化, 因为 RotationDetector 注册顺序.
        cameraDelegate.init()
        layoutDelegate.init()
        canvasDelegate.init()
        gestureDelegate.init()
        surfaceTextureDelegate.init()
    }

    override fun onDetachedFromWindow() {
        surfaceTextureDelegate.dispose()
        gestureDelegate.dispose()
        canvasDelegate.dispose()
        layoutDelegate.dispose()
        cameraDelegate.dispose()
        super.onDetachedFromWindow()
    }
}
