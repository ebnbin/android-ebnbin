package com.ebnbin.windowcamera.view.layout

import com.ebnbin.windowcamera.view.IWindowCameraViewDelegate

interface IWindowCameraViewLayoutDelegate : IWindowCameraViewDelegate {
    /**
     * 更新大小位置, 不会更新 is_out_enabled.
     */
    fun invalidateSizePosition()

    /**
     * Layout 写入偏好.
     */
    fun putPosition(layoutWidth: Int, layoutHeight: Int, layoutX: Int, layoutY: Int)
}
