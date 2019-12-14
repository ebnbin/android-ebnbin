package com.ebnbin.eb.activity

import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.ebnbin.eb.debug.DebugHelper

/**
 * Base Activity.
 */
abstract class EBActivity : AppCompatActivity() {
    private val debugHelper: DebugHelper by lazy { DebugHelper(this) }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (debugHelper.dispatchTouchEvent(ev)) return true
        return super.dispatchTouchEvent(ev)
    }
}
