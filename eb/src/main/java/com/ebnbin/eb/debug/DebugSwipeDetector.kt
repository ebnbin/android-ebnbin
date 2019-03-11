package com.ebnbin.eb.debug

import android.view.MotionEvent
import com.ebnbin.eb.app.EBActivity
import com.ebnbin.eb.debug.event.DebugLeftToRightEvent
import com.ebnbin.eb.debug.event.DebugRightToLeftEvent
import com.ebnbin.eb.debug.event.DebugTopToBottomEvent
import com.ebnbin.eb.library.eventBus
import com.ebnbin.eb.util.dpToPx

/**
 * 用于 debug 的多指滑动检测器.
 */
internal class DebugSwipeDetector(private val ebActivity: EBActivity) {
    /**
     * 滑动距离.
     */
    private val offset: Float = 64f.dpToPx

    /**
     * Debug 模式且非 debug 页面时启用.
     */
    private val isEnabled: Boolean = debug && (ebActivity::class.java != EBActivity::class.java ||
            ebActivity.fragmentClass != DebugFragment::class.java)

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
        if (!isEnabled) return false
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
                leftToRight -> eventBus.post(DebugLeftToRightEvent(ebActivity))
                rightToLeft -> eventBus.post(DebugRightToLeftEvent(ebActivity))
                topToBottom -> eventBus.post(DebugTopToBottomEvent(ebActivity))
                bottomToTop -> DebugFragment.start(ebActivity)
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
}
