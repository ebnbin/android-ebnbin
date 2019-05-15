package com.ebnbin.windowcamera.view.canvas

import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.ebnbin.eb.library.Libraries
import com.ebnbin.eb.util.dpToPx
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.profile.CameraState
import com.ebnbin.windowcamera.profile.CameraStateEvent
import com.ebnbin.windowcamera.profile.ProfileHelper
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class WindowCameraViewCanvasDelegate(private val callback: IWindowCameraViewCanvasCallback) :
    IWindowCameraViewCanvasDelegate,
    SharedPreferences.OnSharedPreferenceChangeListener
{
    override fun init() {
        Libraries.eventBus.register(this)
        ProfileHelper.sharedPreferencesRegister(this)
    }

    override fun dispose() {
        ProfileHelper.sharedPreferencesUnregister(this)
        Libraries.eventBus.unregister(this)
    }

    //*****************************************************************************************************************

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: CameraStateEvent) {
        paint.color = callback.getContext().getColor(COLOR_MAP.getValue(event.cameraState))
        callback.invalidate()
    }

    //*****************************************************************************************************************

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            ProfileHelper.is_border_enabled.key -> {
                callback.invalidate()
            }
        }
    }

    //*****************************************************************************************************************

    private val paint: Paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f.dpToPx
        color = Color.TRANSPARENT
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return
        if (ProfileHelper.is_border_enabled.value) {
            canvas.drawRect(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), paint)
        }
    }

    //*****************************************************************************************************************

    companion object {
        private val COLOR_MAP = mapOf(
            CameraState.CLOSED to R.color.eb_transparent,
            CameraState.OPENING to R.color.eb_material_gray_500,
            CameraState.STARTING_PHOTO_PREVIEW to R.color.eb_material_gray_500,
            CameraState.STARTING_VIDEO_PREVIEW to R.color.eb_material_gray_500,
            CameraState.STARTING_PREVIEW to R.color.eb_material_gray_500,
            CameraState.PREVIEWING_PHOTO to R.color.eb_material_blue_500,
            CameraState.PREVIEWING_VIDEO to R.color.eb_material_green_500,
            CameraState.PREVIEWING to R.color.eb_material_yellow_500,
            CameraState.CAPTURING_VIDEO to R.color.eb_material_red_500
        )
    }
}
