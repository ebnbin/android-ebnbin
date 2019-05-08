package com.ebnbin.windowcamera.debug

import android.os.Bundle
import android.view.View
import com.ebnbin.eb.debug.BaseDebugPageFragment
import com.ebnbin.eb.dev.DevHelper
import com.ebnbin.eb.library.Libraries
import com.ebnbin.windowcamera.dev.Report

/**
 * Debug page 页面.
 */
class DebugPageFragment : BaseDebugPageFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addDebugItem("Report", Libraries.gson.toJson(Report())) {
            DevHelper.report(Report())
        }
    }
}
