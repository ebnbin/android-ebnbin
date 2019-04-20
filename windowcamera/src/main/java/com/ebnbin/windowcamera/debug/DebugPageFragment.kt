package com.ebnbin.windowcamera.debug

import android.Manifest
import android.os.Bundle
import android.view.View
import com.ebnbin.eb.debug.BaseDebugPageFragment
import com.ebnbin.eb.debug.log
import com.ebnbin.eb.permission.PermissionFragment
import com.ebnbin.windowcamera.camera.CameraHelper

/**
 * Debug page 页面.
 */
class DebugPageFragment : BaseDebugPageFragment(), PermissionFragment.Callback {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addDebugItem("CameraHelper") {
            PermissionFragment.start(childFragmentManager, "CameraHelper", listOf(Manifest.permission.CAMERA))
        }
    }

    override fun onPermissionsResult(
        callingId: String,
        permissions: List<String>,
        granted: Boolean,
        extraData: Map<String, Any?>
    ) {
        when (callingId) {
            "CameraHelper" -> {
                if (granted) {
                    try {
                        log(CameraHelper)
                    } catch (throwable: Throwable) {
                        log(throwable)
                    }
                }
            }
        }
    }
}
