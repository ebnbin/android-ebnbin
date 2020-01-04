package com.ebnbin.sample.touch

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.ebnbin.eb.dpToPxRound
import com.ebnbin.eb.getActionMaskedToString
import com.ebnbin.eb.log

class TouchSampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(SampleViewGroup(this), ViewGroup.LayoutParams(dpToPxRound(300f), dpToPxRound(400f)))
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        log("Activity dispatchTouchEvent ${ev?.getActionMaskedToString()} super")
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        log("Activity onTouchEvent ${event?.getActionMaskedToString()} super")
        return super.onTouchEvent(event)
    }

    class SampleViewGroup(context: Context) : FrameLayout(context) {
        init {
            setBackgroundColor(Color.GREEN)
            addView(SampleView(context), ViewGroup.LayoutParams(context.dpToPxRound(100f), context.dpToPxRound(200f)))
            setOnTouchListener { _, event ->
                log("ViewGroup onTouch ${event?.getActionMaskedToString()} false")
                false
            }
        }

        override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
            log("ViewGroup dispatchTouchEvent ${ev?.getActionMaskedToString()} super")
            return super.dispatchTouchEvent(ev)
        }

        override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
            log("ViewGroup onInterceptTouchEvent ${ev?.getActionMaskedToString()} super")
            return super.onInterceptTouchEvent(ev)
        }

        override fun onTouchEvent(event: MotionEvent?): Boolean {
            log("ViewGroup onTouchEvent ${event?.getActionMaskedToString()} super")
            return super.onTouchEvent(event)
        }

        class SampleView(context: Context) : View(context) {
            init {
                setBackgroundColor(Color.RED)
                setOnTouchListener { _, event ->
                    log("View onTouch ${event.getActionMaskedToString()} false")
                    false
                }
//                setOnClickListener {
//                    log("View onClick")
//                }
            }

            override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
                log("View dispatchTouchEvent ${event?.getActionMaskedToString()} super")
                return super.dispatchTouchEvent(event)
            }

            override fun onTouchEvent(event: MotionEvent?): Boolean {
                log("View onTouchEvent ${event?.getActionMaskedToString()} super")
                return super.onTouchEvent(event)
            }
        }
    }
}
