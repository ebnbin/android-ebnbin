package com.ebnbin.eb.debug

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ebnbin.eb.R
import com.ebnbin.eb.app.EBFragment

/**
 * 基础 debug page 页面.
 */
abstract class BaseDebugPageFragment : EBFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.eb_base_debug_page_fragment, container, false)
    }
}
