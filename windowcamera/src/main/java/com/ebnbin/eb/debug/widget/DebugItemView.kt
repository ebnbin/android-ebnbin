package com.ebnbin.eb.debug.widget

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.ebnbin.windowcamera.R
import kotlinx.android.synthetic.main.eb_debug_item_view.view.*

/**
 * Debug 项.
 */
@SuppressLint("ViewConstructor")
class DebugItemView<T> internal constructor(
    context: Context,
    val title: CharSequence,
    var data: T
) : FrameLayout(context) {
    init {
        View.inflate(this.context, R.layout.eb_debug_item_view, this)
        eb_title.text = title
    }

    var summary: CharSequence
        get() = eb_summary.text ?: ""
        set(value) {
            eb_summary.text = value
            eb_summary.visibility = if (value.isEmpty()) View.GONE else View.VISIBLE
        }
}
