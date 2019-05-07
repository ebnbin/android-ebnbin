package com.ebnbin.windowcamera.debug

import android.os.Bundle
import android.view.View
import com.ebnbin.eb.debug.BaseDebugPageFragment
import com.ebnbin.eb.debug.log
import com.ebnbin.eb.util.LibraryHelper
import com.ebnbin.windowcamera.dev.Report

/**
 * Debug page 页面.
 */
class DebugPageFragment : BaseDebugPageFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addDebugItem("CameraHelper") {
            log(LibraryHelper.gson.toJson(Report()))
        }
    }
}
