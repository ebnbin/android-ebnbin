package com.ebnbin.eb.debug

import android.app.Activity
import android.view.MotionEvent
import androidx.core.os.bundleOf
import com.ebnbin.eb.BuildConfig
import com.ebnbin.eb.ebApp
import com.ebnbin.eb.extension.dpToPx
import com.ebnbin.eb.extension.startFragmentByActivity
import com.jeremyliao.liveeventbus.LiveEventBus

class DebugHelper(private val activity: Activity) {
    /**
     * 当指定数量的手指按下时设置为 true. 当最后一个手指抬起时设置为 false.
     */
    private var isSwiping: Boolean = false

    /**
     * 当检测到一次有效滑动或手指数量发生变化时设置为 true. 当最后一个手指抬起时设置为 false.
     */
    private var isSwiped: Boolean = false

    private lateinit var downXYs: Array<Pair<Float, Float>>

    /**
     * @param motionEvent 可能被修改.
     */
    fun dispatchTouchEvent(motionEvent: MotionEvent?): Boolean {
        motionEvent ?: return false
        if (!BuildConfig.DEBUG) return false
        if (motionEvent.actionMasked == MotionEvent.ACTION_CANCEL) {
            // 重置状态.
            isSwiping = false
            isSwiped = false
            return false
        }
        val pointerCount = motionEvent.pointerCount
        if (pointerCount == 3) {
            if (!isSwiping) {
                isSwiping = true
                downXYs = Array(pointerCount) {
                    Pair(motionEvent.getX(it), motionEvent.getY(it))
                }
                // 发送取消事件.
                motionEvent.action = MotionEvent.ACTION_CANCEL
                return false
            }
            if (isSwiped) return false
            // 滑动距离.
            val offset = activity.dpToPx(64f)
            var leftToRight = true
            var rightToLeft = true
            var topToBottom = true
            var bottomToTop = true
            downXYs.forEachIndexed { index, (downX, downY) ->
                val offsetX = motionEvent.getX(index) - downX
                val offsetY = motionEvent.getY(index) - downY
                if (offsetX < offset) leftToRight = false
                if (offsetX > -offset) rightToLeft = false
                if (offsetY < offset) topToBottom = false
                if (offsetY > -offset) bottomToTop = false
            }
            when {
                leftToRight -> LiveEventBus.get(KEY_LEFT_TO_RIGHT).post(Unit)
                rightToLeft -> LiveEventBus.get(KEY_RIGHT_TO_LEFT).post(Unit)
                topToBottom -> LiveEventBus.get(KEY_TOP_TO_BOTTOM).post(Unit)
                bottomToTop -> activity.startFragmentByActivity(ebApp.debugFragmentClass, bundleOf(
                    EBDebugFragment.KEY_CALLING_ACTIVITY to activity.toString()
                ))
                else -> return false
            }
            isSwiped = true
        } else {
            if (!isSwiping) return false
            if (motionEvent.actionMasked == MotionEvent.ACTION_UP) {
                isSwiping = false
                isSwiped = false
            } else if (!isSwiped) {
                isSwiped = true
            }
        }
        return false
    }

    companion object {
        const val KEY_LEFT_TO_RIGHT: String = "left_to_right"
        const val KEY_RIGHT_TO_LEFT: String = "right_to_left"
        const val KEY_TOP_TO_BOTTOM: String = "top_to_bottom"
        const val KEY_BOTTOM_TO_TOP: String = "bottom_to_top"
    }
}
