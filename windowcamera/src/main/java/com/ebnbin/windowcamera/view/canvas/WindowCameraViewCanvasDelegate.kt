package com.ebnbin.windowcamera.view.canvas

import android.graphics.Canvas
import android.graphics.Paint
import com.ebnbin.eb.util.dpToPx
import com.ebnbin.windowcamera.R

class WindowCameraViewCanvasDelegate(private val callback: IWindowCameraViewCanvasCallback) :
    IWindowCameraViewCanvasDelegate
{
    override fun init() {
    }

    override fun dispose() {
    }

    //*****************************************************************************************************************

    private val paint: Paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f.dpToPx
        color = callback.getContext().getColor(R.color.eb_material_light_blue_500)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return
        canvas.drawRect(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), paint)
    }
}
