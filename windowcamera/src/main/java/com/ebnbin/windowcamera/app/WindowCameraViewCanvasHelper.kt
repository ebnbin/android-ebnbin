package com.ebnbin.windowcamera.app

import android.graphics.Canvas
import android.graphics.Paint
import com.ebnbin.eb.util.dpToPx
import com.ebnbin.windowcamera.R

class WindowCameraViewCanvasHelper(private val windowCameraView: WindowCameraView) {
    private val paint: Paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f.dpToPx
        color = windowCameraView.context.resources.getColor(R.color.eb_material_light_blue_500,
            windowCameraView.context.theme)
    }

    fun onDraw(canvas: Canvas?) {
        canvas ?: return
        canvas.drawRect(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), paint)
    }
}
