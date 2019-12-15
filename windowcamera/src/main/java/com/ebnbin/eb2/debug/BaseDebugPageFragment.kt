package com.ebnbin.eb2.debug

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ebnbin.windowcamera.R
import com.ebnbin.eb2.debug.widget.DebugItemView
import com.ebnbin.eb2.fragment.EBFragment
import kotlinx.android.synthetic.main.eb_base_debug_page_fragment.*

/**
 * 基础 debug page 页面.
 */
abstract class BaseDebugPageFragment : EBFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.eb_base_debug_page_fragment, container, false)
    }

    protected fun addDebugItem(
        title: CharSequence,
        summary: CharSequence = "",
        onClick: ((DebugItemView<Unit>) -> Unit)? = null
    ): DebugItemView<Unit> {
        return addDebugItem(title, summary, Unit, onClick)
    }

    protected fun <T> addDebugItem(
        title: CharSequence,
        summary: CharSequence = "",
        data: T,
        onClick: ((DebugItemView<T>) -> Unit)? = null
    ): DebugItemView<T> {
        val debugItemView = DebugItemView(requireContext(), title, data)
        debugItemView.summary = summary
        debugItemView.setOnClickListener {
            onClick?.invoke(debugItemView)
        }
        eb_linear_layout.addView(debugItemView)
        return debugItemView
    }
}