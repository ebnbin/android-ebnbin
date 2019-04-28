package com.ebnbin.windowcamera.view.gesture

import android.view.GestureDetector
import android.view.MotionEvent
import androidx.core.view.GestureDetectorCompat
import com.ebnbin.windowcamera.profile.ProfileHelper
import com.ebnbin.windowcamera.view.WindowCameraView

class WindowCameraViewGestureDelegate(private val callback: IWindowCameraViewGestureCallback) :
    IWindowCameraViewGestureDelegate,
    GestureDetector.OnGestureListener,
    GestureDetector.OnDoubleTapListener
{
    override fun init(windowCameraView: WindowCameraView) {
    }

    override fun dispose() {
    }

    //*****************************************************************************************************************

    private val gestureDetector: GestureDetectorCompat = GestureDetectorCompat(callback.getContext(), this)

    /**
     * 按下时窗口的 layout x.
     */
    private var downLayoutX: Int = 0
    /**
     * 按下时窗口的 layout y.
     */
    private var downLayoutY: Int = 0
    /**
     * 按下时手指在屏幕的 x.
     */
    private var downRawX: Float = 0f
    /**
     * 按下时手指在屏幕的 y.
     */
    private var downRawY: Float = 0f

    //*****************************************************************************************************************

    override fun onTouchEvent(motionEvent: MotionEvent?): Boolean {
        motionEvent ?: return false
        return gestureDetector.onTouchEvent(motionEvent)
    }

    //*****************************************************************************************************************

    override fun onDown(e: MotionEvent?): Boolean {
        e ?: return false
        downLayoutX = callback.getLayoutX()
        downLayoutY = callback.getLayoutY()
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
        // 缓存 is_move_enabled.
        if (!ProfileHelper.is_move_enabled.value) return false
        val layoutX = downLayoutX + e2.rawX - downRawX
        val layoutY = downLayoutY + e2.rawY - downRawY
        callback.onMove(layoutX, layoutY)
        return false
    }

    override fun onLongPress(e: MotionEvent?) {
        callback.onLongPress()
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        return false
    }

    //*****************************************************************************************************************

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        callback.onSingleTap()
        return false
    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        callback.onDoubleTap()
        return false
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        return false
    }
}
