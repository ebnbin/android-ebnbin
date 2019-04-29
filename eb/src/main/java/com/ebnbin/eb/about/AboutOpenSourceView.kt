package com.ebnbin.eb.about

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.ebnbin.eb.R
import kotlinx.android.synthetic.main.eb_about_open_source_view.view.*

class AboutOpenSourceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {
    init {
        View.inflate(context, R.layout.eb_about_open_source_view, this)
    }

    fun setData(pair: Pair<String, String>) {
        eb_name.text = pair.first
        eb_url.text = pair.second
        setOnClickListener {
            // TODO
        }
    }
}
