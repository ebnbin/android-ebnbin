package com.ebnbin.windowcamera.view.gesture

import android.view.MotionEvent
import com.ebnbin.windowcamera.view.IWindowCameraViewDelegate

interface IWindowCameraViewGestureDelegate : IWindowCameraViewDelegate {
    /**
     * 触摸事件.
     */
    fun onTouchEvent(motionEvent: MotionEvent?): Boolean
}
