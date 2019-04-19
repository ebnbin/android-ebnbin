package com.ebnbin.windowcamera.debug

import android.os.Bundle
import android.view.View
import com.ebnbin.eb.debug.BaseDebugPageFragment
import com.ebnbin.eb.debug.log
import com.ebnbin.windowcamera.camera.CameraHelper

/**
 * Debug page 页面.
 */
class DebugPageFragment : BaseDebugPageFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addDebugItem("CameraHelper") {
            try {
                log(CameraHelper)
            } catch (throwable: Throwable) {
                // 需要 catch Throwable 避免 ExceptionInInitializerError.
            }
        }
    }
}
