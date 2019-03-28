package com.ebnbin.windowcamera.debug

import android.os.Bundle
import android.view.View
import com.ebnbin.eb.debug.BaseDebugPageFragment
import com.ebnbin.windowcamera.service.WindowCameraService

/**
 * Debug page 页面.
 */
class DebugPageFragment : BaseDebugPageFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addDebugItem("Start WindowCameraService") {
            WindowCameraService.start(requireContext())
        }

        addDebugItem("Stop WindowCameraService") {
            WindowCameraService.stop(requireContext())
        }
    }
}
