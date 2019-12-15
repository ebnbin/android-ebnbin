package com.ebnbin.windowcamera.debug

import com.ebnbin.eb2.debug.EBDebugPageFragment
import com.ebnbin.windowcamera.dev.Report

/**
 * Debug page 页面.
 */
class DebugPageFragment : EBDebugPageFragment() {
    override fun onAddDevItems() {
        addDevItem("Report") {
            it.summary.value = Report().create().toString()
        }
        super.onAddDevItems()
    }
}
