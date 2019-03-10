package com.ebnbin.eb.app

import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.ebnbin.eb.R
import com.ebnbin.eb.debug.DebugSwipeDetector

/**
 * Base Activity.
 */
abstract class EBActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_AppCompat)
    }

    //*****************************************************************************************************************

    private val debugSwipeDetector: DebugSwipeDetector = DebugSwipeDetector()

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (debugSwipeDetector.dispatchTouchEvent(ev)) return true
        return super.dispatchTouchEvent(ev)
    }
}
