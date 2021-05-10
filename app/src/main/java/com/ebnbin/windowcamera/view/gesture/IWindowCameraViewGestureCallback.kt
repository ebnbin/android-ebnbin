package com.ebnbin.windowcamera.view.gesture

import com.ebnbin.windowcamera.profile.enumeration.ProfileGesture
import com.ebnbin.windowcamera.view.IWindowCameraViewCallback

interface IWindowCameraViewGestureCallback : IWindowCameraViewCallback {
    /**
     * 返回 WindowManager.LayoutParams.x.
     */
    fun getLayoutX(): Int

    /**
     * 返回 WindowManager.LayoutParams.y.
     */
    fun getLayoutY(): Int

    /**
     * 移动手势. 移动过程中会不断调用.
     *
     * @param layoutX 移动到的 WindowManager.LayoutParams.x.
     *
     * @param layoutY 移动到的 WindowManager.LayoutParams.y.
     */
    fun onMove(layoutX: Int, layoutY: Int)

    fun onGesture(profileGesture: ProfileGesture)
}
