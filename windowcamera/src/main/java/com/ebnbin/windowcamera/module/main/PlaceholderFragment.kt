package com.ebnbin.windowcamera.module.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.NestedScrollView
import com.ebnbin.eb.app.EBFragment

/**
 * 占位 Fragment.
 */
open class PlaceholderFragment : EBFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val nestedScrollView = NestedScrollView(requireContext())
        val textView = AppCompatTextView(requireContext())
        val sb = StringBuilder()
        for (i in 0 until 256) {
            sb.appendln(i)
        }
        textView.text = sb
        nestedScrollView.addView(textView)
        return nestedScrollView
    }
}
