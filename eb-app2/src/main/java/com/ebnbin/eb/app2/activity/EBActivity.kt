package com.ebnbin.eb.app2.activity

import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.ebnbin.eb.app2.dev.DevActivitySwipeDetector

/**
 * 基础 Activity.
 */
abstract class EBActivity : AppCompatActivity() {
    private val devActivitySwipeDetector: DevActivitySwipeDetector by lazy {
        DevActivitySwipeDetector(
            this
        )
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (devActivitySwipeDetector.dispatchTouchEvent(ev)) return true
        return super.dispatchTouchEvent(ev)
    }
}
