package com.ebnbin.windowcamera.view.canvas

import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Paint
import com.ebnbin.eb.EBApp
import com.ebnbin.eb.extension.dpToPx
import com.ebnbin.eb2.library.Libraries
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.profile.CameraState
import com.ebnbin.windowcamera.profile.CameraStateEvent
import com.ebnbin.windowcamera.profile.ProfileHelper
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.math.min

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
        callback.invalidate()
    }

    //*****************************************************************************************************************

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            ProfileHelper.radius.key -> {
                callback.invalidate()
            }
            ProfileHelper.is_border_enabled.key -> {
                callback.invalidate()
            }
            ProfileHelper.border_width.key -> {
                callback.invalidate()
            }
        }
    }

    //*****************************************************************************************************************

    private val paint: Paint = Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return
        if (ProfileHelper.is_border_enabled.value) {
            paint.color = callback.getContext().getColor(COLOR_MAP.getValue(ProfileHelper.cameraState))
            // * 2f 是因为边框是居中的，可见宽度是实际宽度的一半.
            paint.strokeWidth = EBApp.instance.dpToPx(ProfileHelper.border_width.value.toFloat()) * 2f
            // 需要 - 1f （一个小的浮点数）否则显示圆角异常.
            val radius = min(canvas.width, canvas.height) * ProfileHelper.radius.value / 200f - 1f
            canvas.drawRoundRect(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), radius, radius, paint)
        }
    }

    //*****************************************************************************************************************

    companion object {
        private val COLOR_MAP = mapOf(
            CameraState.CLOSED to R.color.eb_transparent,
            CameraState.STATING to R.color.eb_material_gray_500,
            CameraState.PREVIEWING_PHOTO to R.color.eb_material_blue_500,
            CameraState.PREVIEWING_VIDEO to R.color.eb_material_green_500,
            CameraState.PREVIEWING_ONLY to R.color.eb_material_yellow_500,
            CameraState.CAPTURING_VIDEO to R.color.eb_material_red_500
        )
    }
}
