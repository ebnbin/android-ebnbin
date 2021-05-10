package com.ebnbin.eb

import android.view.MotionEvent

fun MotionEvent.getActionMaskedToString(): String {
    return when(val actionMasked = actionMasked) {
        MotionEvent.ACTION_DOWN -> "ACTION_DOWN" // 0
        MotionEvent.ACTION_UP -> "ACTION_UP" // 1
        MotionEvent.ACTION_MOVE -> "ACTION_MOVE" // 2
        MotionEvent.ACTION_CANCEL -> "ACTION_CANCEL" // 3
        MotionEvent.ACTION_OUTSIDE -> "ACTION_OUTSIDE" // 4
        MotionEvent.ACTION_POINTER_DOWN -> "ACTION_POINTER_DOWN" // 5
        MotionEvent.ACTION_POINTER_UP -> "ACTION_POINTER_UP" // 6
        MotionEvent.ACTION_HOVER_MOVE -> "ACTION_HOVER_MOVE" // 7
        MotionEvent.ACTION_SCROLL -> "ACTION_SCROLL" // 8
        MotionEvent.ACTION_HOVER_ENTER -> "ACTION_HOVER_ENTER" // 9
        MotionEvent.ACTION_HOVER_EXIT -> "ACTION_HOVER_EXIT" // 10
        MotionEvent.ACTION_BUTTON_PRESS -> "ACTION_BUTTON_PRESS" // 11
        MotionEvent.ACTION_BUTTON_RELEASE -> "ACTION_BUTTON_RELEASE" // 12
        else -> "$actionMasked"
    }
}
