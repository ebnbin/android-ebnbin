package com.ebnbin.windowcamera.view

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.WindowManager
import androidx.core.view.GestureDetectorCompat
import com.ebnbin.windowcamera.profile.ProfileHelper

class WindowCameraViewGestureDelegate(private val windowCameraView: WindowCameraView) :
    GestureDetector.OnGestureListener,
    GestureDetector.OnDoubleTapListener
{
    private val gestureDetector: GestureDetectorCompat = GestureDetectorCompat(windowCameraView.context, this)

    var isMoveEnabled: Boolean = ProfileHelper.is_move_enabled.value

    private var downLayoutX: Float = 0f
    private var downLayoutY: Float = 0f
    private var downRawX: Float = 0f
    private var downRawY: Float = 0f

    //*****************************************************************************************************************

    fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false
        return gestureDetector.onTouchEvent(event)
    }

    //*****************************************************************************************************************

    override fun onDown(e: MotionEvent?): Boolean {
        e ?: return false
        val layoutParams = windowCameraView.layoutParams as WindowManager.LayoutParams
        downLayoutX = layoutParams.x.toFloat()
        downLayoutY = layoutParams.y.toFloat()
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
        if (isMoveEnabled) {
            val x = downLayoutX + e2.rawX - downRawX
            val y = downLayoutY + e2.rawY - downRawY
            windowCameraView.onMove(x, y)
        }
        return false
    }

    override fun onLongPress(e: MotionEvent?) {
        windowCameraView.onLongPress()
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        return false
    }

    //*****************************************************************************************************************

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        windowCameraView.onSingleTap()
        return false
    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        windowCameraView.onDoubleTap()
        return false
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        return false
    }
}
