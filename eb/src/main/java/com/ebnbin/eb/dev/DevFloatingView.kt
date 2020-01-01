package com.ebnbin.eb.dev

import android.annotation.SuppressLint
import android.content.Context
import android.view.GestureDetector
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.GestureDetectorCompat
import com.ebnbin.eb.R
import kotlin.math.roundToInt

internal class DevFloatingView(context: Context) :
    FrameLayout(context), GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    init {
        View.inflate(this.context, R.layout.eb_dev_floating_view, this)
    }

    private val gestureDetector: GestureDetectorCompat = GestureDetectorCompat(context, this)

    //*****************************************************************************************************************

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return super.onTouchEvent(event)
        if (gestureDetector.onTouchEvent(event)) return true
        return super.onTouchEvent(event)
    }

    //*****************************************************************************************************************

    private var downRawOffsetX: Float = 0f
    private var downRawOffsetY: Float = 0f

    //*****************************************************************************************************************

    override fun onDown(e: MotionEvent?): Boolean {
        e ?: return false
        val locationOnScreen = IntArray(2).also { getLocationOnScreen(it) }
        downRawOffsetX = locationOnScreen[0] - e.rawX
        downRawOffsetY = locationOnScreen[1] - e.rawY
        return true
    }

    override fun onShowPress(e: MotionEvent?) {
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return false
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        e2 ?: return false
        listener?.onScroll((downRawOffsetX + e2.rawX).roundToInt(), (downRawOffsetY + e2.rawY).roundToInt())
        return false
    }

    override fun onLongPress(e: MotionEvent?) {
        if (listener?.onLongPress() == true) {
            performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        }
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        return false
    }

    //*****************************************************************************************************************

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        listener?.onSingleTap()
        return false
    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        listener?.onDoubleTap()
        return false
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        return false
    }

    //*****************************************************************************************************************

    private var listener: Listener? = null

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    interface Listener {
        fun onScroll(x: Int, y: Int) = Unit

        fun onLongPress(): Boolean = false

        fun onSingleTap() = Unit

        fun onDoubleTap() = Unit
    }
}
