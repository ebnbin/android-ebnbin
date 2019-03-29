package com.ebnbin.windowcamera.debug

import android.os.Bundle
import android.view.View
import com.ebnbin.eb.debug.BaseDebugPageFragment
import com.ebnbin.eb.permission.PermissionFragment
import com.ebnbin.windowcamera.camera.CameraHelper
import com.ebnbin.windowcamera.service.WindowCameraService

/**
 * Debug page 页面.
 */
class DebugPageFragment : BaseDebugPageFragment(), PermissionFragment.Callback {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addDebugItem("CameraHelper") {
            try {
                CameraHelper
            } catch (throwable: Throwable) {
                // 需要 catch Throwable 避免 ExceptionInInitializerError.
            }
        }

        addDebugItem("Start WindowCameraService") {
            PermissionFragment.start(childFragmentManager, "WindowCameraService", WindowCameraService.permissions)
        }

        addDebugItem("Stop WindowCameraService") {
            WindowCameraService.stop(requireContext())
        }
    }

    override fun onPermissionsResult(
        callingId: String,
        permissions: List<String>,
        granted: Boolean,
        extraData: Map<String, Any?>
    ) {
        if (!granted) return
        when (callingId) {
            "WindowCameraService" -> {
                WindowCameraService.start(requireContext())
            }
        }
    }
}
