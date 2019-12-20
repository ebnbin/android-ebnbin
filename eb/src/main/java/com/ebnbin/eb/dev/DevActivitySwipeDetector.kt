package com.ebnbin.eb.dev

import android.app.Activity
import android.view.MotionEvent
import androidx.core.os.bundleOf
import com.ebnbin.eb.EBApp
import com.ebnbin.eb.fragment.openFragment
import com.ebnbin.eb.util.dpToPx

/**
 * 开发者选项滑动检测器. 在 Activity 三指上滑打开 Dev 页面.
 */
internal class DevActivitySwipeDetector(private val activity: Activity) {
    /**
     * 当 3 个手指按下时设置为 true, 当最后一个手指抬起时设置为 false.
     */
    private var isSwiping: Boolean = false
    /**
     * 当完成一次有效滑动或手指数量发生变化时设置为 true, 当最后一个手指抬起时设置为 false.
     */
    private var isSwiped: Boolean = false

    private lateinit var downXYs: Array<Pair<Float, Float>>

    /**
     * @param motionEvent action 可能被修改.
     */
    fun dispatchTouchEvent(motionEvent: MotionEvent?): Boolean {
        motionEvent ?: return false
        if (!DevUtil.dev) return false
        if (motionEvent.actionMasked == MotionEvent.ACTION_CANCEL) {
            // 取消事件, 重置状态.
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
            val offset = activity.dpToPx(64f)
            var toLeft = true
            var toRight = true
            var toTop = true
            var toBottom = true
            downXYs.forEachIndexed { index, (downX, downY) ->
                val offsetX = motionEvent.getX(index) - downX
                val offsetY = motionEvent.getY(index) - downY
                if (offsetX > -offset) toLeft = false
                if (offsetX < offset) toRight = false
                if (offsetY > -offset) toTop = false
                if (offsetY < offset) toBottom = false
            }
            when {
                toLeft -> Unit
                toRight -> Unit
                toTop -> activity.openFragment(EBApp.instance.devFragmentClass, bundleOf(
                    DevFragment.KEY_CALLING_ACTIVITY to activity.toString()
                ))
                toBottom -> Unit
                else -> return false
            }
            isSwiped = true
        } else {
            if (!isSwiping) return false
            if (motionEvent.actionMasked == MotionEvent.ACTION_UP) {
                // 手指抬起, 重置状态.
                isSwiping = false
                isSwiped = false
            } else if (!isSwiped) {
                isSwiped = true
            }
        }
        return false
    }
}
