package com.ebnbin.windowcamera.view.canvas

import android.graphics.Canvas
import com.ebnbin.windowcamera.view.IWindowCameraViewDelegate

/**
 * 画布代理.
 */
interface IWindowCameraViewCanvasDelegate : IWindowCameraViewDelegate {
    /**
     * 绘制.
     */
    fun onDraw(canvas: Canvas?)
}
