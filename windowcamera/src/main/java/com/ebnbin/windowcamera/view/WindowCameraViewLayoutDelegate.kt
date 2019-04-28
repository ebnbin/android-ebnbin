package com.ebnbin.windowcamera.view

import android.view.WindowManager
import com.ebnbin.eb.util.RotationDetector
import com.ebnbin.eb.util.RotationSize
import com.ebnbin.eb.util.WindowHelper
import com.ebnbin.windowcamera.profile.ProfileHelper
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class WindowCameraViewLayoutDelegate(private val windowCameraView: WindowCameraView) : RotationDetector.Listener {
    fun init() {
        RotationDetector.register(this)
        invalidateLayout(invalidateIsOutEnabled = true, invalidateSize = true)
        invalidateAlpha()
        invalidateIsKeepScreenOnEnabled()
        invalidateIsTouchable()
    }

    fun dispose() {
        RotationDetector.unregister(this)
    }

    //*****************************************************************************************************************

    var displaySize: RotationSize = if (ProfileHelper.is_out_enabled.value) WindowHelper.displayRealSize else
        WindowHelper.displaySize
        private set

    override fun onRotationChanged(oldRotation: Int, newRotation: Int) {
        displaySize = if (ProfileHelper.is_out_enabled.value) WindowHelper.displayRealSize else
            WindowHelper.displaySize
        invalidateLayout(invalidateIsOutEnabled = false, invalidateSize = true)
        windowCameraView.cameraDelegate.invalidateTransform()
    }

    //*****************************************************************************************************************

    fun invalidateLayout(invalidateIsOutEnabled: Boolean, invalidateSize: Boolean) {
        windowCameraView.updateLayoutParams {
            // 参数不合法.
            if (invalidateIsOutEnabled && !invalidateSize) throw RuntimeException()

            fun calcPosition(range: Int, percent: Int, offset: Int): Int {
                return (range * percent / 100f + offset).roundToInt()
            }

            val isOutEnabled = ProfileHelper.is_out_enabled.value
            if (invalidateIsOutEnabled) {
                flags = if (isOutEnabled) {
                    flags or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                } else {
                    flags or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS xor
                            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                }
                displaySize = if (ProfileHelper.is_out_enabled.value) WindowHelper.displayRealSize else
                    WindowHelper.displaySize
            }
            val rotationSize: RotationSize
            if (invalidateIsOutEnabled || invalidateSize) {
                val sizeSp = ProfileHelper.size.value
                val ratio = windowCameraView.getRatioBySp()
                rotationSize = displaySize.crop(ratio, sizeSp / 100f)
                width = rotationSize.width
                height = rotationSize.height
            } else {
                rotationSize = RotationSize(width, height, displaySize.rotation)
            }
            val xSp = ProfileHelper.x().value
            val xRange: Int
            val xPercent: Int
            val xOffset: Int
            when (xSp) {
                in 0..100 -> {
                    xRange = displaySize.width - rotationSize.width
                    xPercent = xSp
                    xOffset = 0
                }
                in -99..-1 -> {
                    xRange = rotationSize.width
                    xPercent = xSp + 100
                    xOffset = -rotationSize.width
                }
                in 101..199 -> {
                    xRange = rotationSize.width
                    xPercent = xSp - 100
                    xOffset = displaySize.width - rotationSize.width
                }
                else -> throw RuntimeException()
            }
            x = calcPosition(xRange, xPercent, xOffset)
            val ySp = ProfileHelper.y().value
            val yRange: Int
            val yPercent: Int
            val yOffset: Int
            when (ySp) {
                in 0..100 -> {
                    yRange = displaySize.height - rotationSize.height
                    yPercent = ySp
                    yOffset = 0
                }
                in -99..-1 -> {
                    yRange = rotationSize.height
                    yPercent = ySp + 100
                    yOffset = -rotationSize.height
                }
                in 101..199 -> {
                    yRange = rotationSize.height
                    yPercent = ySp - 100
                    yOffset = displaySize.height - rotationSize.height
                }
                else -> throw RuntimeException()
            }
            y = calcPosition(yRange, yPercent, yOffset)
        }
    }

    fun putPosition(layoutWidth: Int, layoutHeight: Int, x: Float, y: Float) {
        fun calcPositionPercent(position: Float, range: Int, percentOffset: Int, isOutEnabled: Boolean): Int {
            var positionPercent = (position / range * 100).toInt() + percentOffset
            positionPercent = min(positionPercent, if (isOutEnabled) 199 else 100)
            positionPercent = max(positionPercent, if (isOutEnabled) -99 else 0)
            return positionPercent
        }

        val isOutEnabled = ProfileHelper.is_out_enabled.value
        val displaySize = if (isOutEnabled) WindowHelper.displayRealSize else WindowHelper.displaySize

        val xMin = 0
        val xMax = displaySize.width - layoutWidth
        val xPosition: Float
        val xRange: Int
        val xPercentOffset: Int
        when {
            x in xMin.toFloat()..xMax.toFloat() -> {
                xPosition = x
                xRange = xMax - xMin
                xPercentOffset = 0
            }
            x < xMin -> {
                xPosition = x + layoutWidth
                xRange = layoutWidth
                xPercentOffset = -100
            }
            else -> {
                xPosition = x + layoutWidth - displaySize.width
                xRange = layoutWidth
                xPercentOffset = 100
            }
        }
        val xPercent = calcPositionPercent(xPosition, xRange, xPercentOffset, isOutEnabled)

        val yMin = 0
        val yMax = displaySize.height - layoutHeight
        val yPosition: Float
        val yRange: Int
        val yPercentOffset: Int
        when {
            y in yMin.toFloat()..yMax.toFloat() -> {
                yPosition = y
                yRange = yMax - yMin
                yPercentOffset = 0
            }
            y < yMin -> {
                yPosition = y + layoutHeight
                yRange = layoutHeight
                yPercentOffset = -100
            }
            else -> {
                yPosition = y + layoutHeight - displaySize.height
                yRange = layoutHeight
                yPercentOffset = 100
            }
        }
        val yPercent = calcPositionPercent(yPosition, yRange, yPercentOffset, isOutEnabled)

        val xSp = ProfileHelper.x().value
        val ySp = ProfileHelper.y().value
        if (xPercent != xSp || yPercent != ySp) {
            ProfileHelper.putXY(xPercent, yPercent)
        }
    }

    //*****************************************************************************************************************

    fun invalidateAlpha() {
        windowCameraView.updateLayoutParams {
            alpha = ProfileHelper.alpha.value / 100f
        }
    }

    fun invalidateIsKeepScreenOnEnabled() {
        windowCameraView.updateLayoutParams {
            flags = if (ProfileHelper.is_keep_screen_on_enabled.value) {
                flags or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            } else {
                flags or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON xor
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            }
        }
    }

    fun invalidateIsTouchable() {
        windowCameraView.updateLayoutParams {
            flags = if (ProfileHelper.is_touchable.value) {
                flags or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE xor
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            } else {
                flags or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            }
        }
    }
}
