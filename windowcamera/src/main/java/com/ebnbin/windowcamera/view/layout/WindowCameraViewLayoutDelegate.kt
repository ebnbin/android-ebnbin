package com.ebnbin.windowcamera.view.layout

import android.content.SharedPreferences
import android.view.WindowManager
import com.ebnbin.eb.util.Ratio
import com.ebnbin.eb.util.RotationDetector
import com.ebnbin.eb.util.RotationSize
import com.ebnbin.eb.util.WindowHelper
import com.ebnbin.windowcamera.profile.ProfileHelper
import com.ebnbin.windowcamera.profile.enumeration.ProfileRatio
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * 需要早于 WindowCameraViewSurfaceDelegate 初始化.
 */
class WindowCameraViewLayoutDelegate(private val callback: IWindowCameraViewLayoutCallback) :
    IWindowCameraViewLayoutDelegate,
    SharedPreferences.OnSharedPreferenceChangeListener,
    RotationDetector.Listener
{
    override fun init() {
        ProfileHelper.sharedPreferencesRegister(this)
        RotationDetector.register(this)
        invalidateSizePosition(SizePosition.ALL)
        invalidateAlpha()
        invalidateIsKeepScreenOnEnabled()
        invalidateIsTouchable()
    }

    override fun dispose() {
        RotationDetector.unregister(this)
        ProfileHelper.sharedPreferencesUnregister(this)
    }

    //*****************************************************************************************************************

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            ProfileHelper.size.key -> {
                invalidateSizePosition(SizePosition.SIZE_POSITION)
            }
            ProfileHelper.ratio.key -> {
                invalidateSizePosition(SizePosition.SIZE_POSITION)
            }
            ProfileHelper.is_out_enabled.key -> {
                invalidateSizePosition(SizePosition.ALL)
            }
            ProfileHelper.in_x.key -> {
                invalidateSizePosition(SizePosition.POSITION)
            }
            ProfileHelper.in_y.key -> {
                invalidateSizePosition(SizePosition.POSITION)
            }
            ProfileHelper.out_x.key -> {
                invalidateSizePosition(SizePosition.POSITION)
            }
            ProfileHelper.out_y.key -> {
                invalidateSizePosition(SizePosition.POSITION)
            }
            ProfileHelper.alpha.key -> {
                invalidateAlpha()
            }
            ProfileHelper.is_keep_screen_on_enabled.key -> {
                invalidateIsKeepScreenOnEnabled()
            }
            ProfileHelper.is_touchable.key -> {
                invalidateIsTouchable()
            }
        }
    }

    //*****************************************************************************************************************

    override fun onRotationChanged(oldRotation: Int, newRotation: Int) {
        invalidateSizePosition(SizePosition.SIZE_POSITION)
    }

    //*****************************************************************************************************************

    private enum class SizePosition {
        /**
         * 更新 is_out_enabled, 同时会更新大小和位置.
         */
        ALL,
        /**
         * 不更新 is_out_enabled, 只更新大小, 同时会更新位置.
         */
        SIZE_POSITION,
        /**
         * 不更新 is_out_enabled 和大小, 只更新位置.
         */
        POSITION
    }

    override fun invalidateSizePosition() {
        invalidateSizePosition(SizePosition.SIZE_POSITION)
    }

    private fun invalidateSizePosition(sizePosition: SizePosition) {
        callback.updateLayoutParams {
            fun calcPosition(range: Int, percent: Int, offset: Int): Int {
                return (range * percent / 100f + offset).roundToInt()
            }

            if (sizePosition == SizePosition.ALL) {
                flags = if (ProfileHelper.is_out_enabled.value) {
                    flags or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                } else {
                    flags or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS xor
                            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                }
            }

            val displaySize = if (ProfileHelper.is_out_enabled.value) WindowHelper.displayRealSize else
                WindowHelper.displaySize
            val rotationSize: RotationSize
            if (sizePosition == SizePosition.POSITION) {
                rotationSize = RotationSize(width, height, displaySize.rotation)
            } else {
                val ratio0 = when (ProfileRatio.get()) {
                    ProfileRatio.CAPTURE -> ProfileHelper.resolution().ratio0
                    ProfileRatio.SCREEN -> displaySize.ratio0
                    ProfileRatio.SQUARE -> Ratio.SQUARE
                }
                rotationSize = displaySize.crop(ratio0, ProfileHelper.size.value / 100f)
                width = rotationSize.width
                height = rotationSize.height
            }

            val xSp = ProfileHelper.x()
            val xRange: Int
            val xPercent: Int
            val xOffset: Int
            when (xSp.value) {
                in 0..100 -> {
                    xRange = displaySize.width - rotationSize.width
                    xPercent = xSp.value
                    xOffset = 0
                }
                in -99..-1 -> {
                    xRange = rotationSize.width
                    xPercent = xSp.value + 100
                    xOffset = -rotationSize.width
                }
                in 101..199 -> {
                    xRange = rotationSize.width
                    xPercent = xSp.value - 100
                    xOffset = displaySize.width - rotationSize.width
                }
                else -> throw RuntimeException()
            }
            x = calcPosition(xRange, xPercent, xOffset)

            val ySp = ProfileHelper.y()
            val yRange: Int
            val yPercent: Int
            val yOffset: Int
            when (ySp.value) {
                in 0..100 -> {
                    yRange = displaySize.height - rotationSize.height
                    yPercent = ySp.value
                    yOffset = 0
                }
                in -99..-1 -> {
                    yRange = rotationSize.height
                    yPercent = ySp.value + 100
                    yOffset = -rotationSize.height
                }
                in 101..199 -> {
                    yRange = rotationSize.height
                    yPercent = ySp.value - 100
                    yOffset = displaySize.height - rotationSize.height
                }
                else -> throw RuntimeException()
            }
            y = calcPosition(yRange, yPercent, yOffset)
        }
    }

    override fun putPosition(layoutWidth: Int, layoutHeight: Int, layoutX: Int, layoutY: Int) {
        fun calcPositionPercent(
            position: Int,
            range: Int,
            percentOffset: Int,
            isOutEnabled: Boolean,
            defaultValue: Int
        ): Int {
            if (range <= 0) return defaultValue
            var positionPercent = (position.toFloat() / range * 100f).roundToInt() + percentOffset
            positionPercent = min(positionPercent, if (isOutEnabled) 199 else 100)
            positionPercent = max(positionPercent, if (isOutEnabled) -99 else 0)
            return positionPercent
        }

        val displaySize = if (ProfileHelper.is_out_enabled.value) WindowHelper.displayRealSize else
            WindowHelper.displaySize

        val xMin = 0
        val xMax = displaySize.width - layoutWidth
        val xPosition: Int
        val xRange: Int
        val xPercentOffset: Int
        when {
            layoutX in xMin..xMax -> {
                xPosition = layoutX
                xRange = xMax - xMin
                xPercentOffset = 0
            }
            layoutX < xMin -> {
                xPosition = layoutX + layoutWidth
                xRange = layoutWidth
                xPercentOffset = -100
            }
            else -> {
                xPosition = layoutX + layoutWidth - displaySize.width
                xRange = layoutWidth
                xPercentOffset = 100
            }
        }
        val xPercent = calcPositionPercent(xPosition, xRange, xPercentOffset, ProfileHelper.is_out_enabled.value,
            ProfileHelper.x().value)

        val yMin = 0
        val yMax = displaySize.height - layoutHeight
        val yPosition: Int
        val yRange: Int
        val yPercentOffset: Int
        when {
            layoutY in yMin..yMax -> {
                yPosition = layoutY
                yRange = yMax - yMin
                yPercentOffset = 0
            }
            layoutY < yMin -> {
                yPosition = layoutY + layoutHeight
                yRange = layoutHeight
                yPercentOffset = -100
            }
            else -> {
                yPosition = layoutY + layoutHeight - displaySize.height
                yRange = layoutHeight
                yPercentOffset = 100
            }
        }
        val yPercent = calcPositionPercent(yPosition, yRange, yPercentOffset, ProfileHelper.is_out_enabled.value,
            ProfileHelper.y().value)

        if (xPercent != ProfileHelper.x().value || yPercent != ProfileHelper.y().value) {
            ProfileHelper.putXY(xPercent, yPercent)
        }
    }

    //*****************************************************************************************************************

    private fun invalidateAlpha() {
        callback.updateLayoutParams {
            alpha = ProfileHelper.alpha.value / 100f
        }
    }

    private fun invalidateIsKeepScreenOnEnabled() {
        callback.updateLayoutParams {
            flags = if (ProfileHelper.is_keep_screen_on_enabled.value) {
                flags or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            } else {
                flags or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON xor
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            }
        }
    }

    private fun invalidateIsTouchable() {
        callback.updateLayoutParams {
            flags = if (ProfileHelper.is_touchable.value) {
                flags or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE xor
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            } else {
                flags or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            }
        }
    }
}
