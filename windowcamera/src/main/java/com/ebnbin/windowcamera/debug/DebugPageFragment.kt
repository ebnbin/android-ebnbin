package com.ebnbin.windowcamera.debug

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.ebnbin.eb.debug.BaseDebugPageFragment
import com.ebnbin.eb.debug.log
import com.ebnbin.eb.permission.PermissionFragment
import com.ebnbin.eb.util.Consts
import com.ebnbin.windowcamera.camera.CameraHelper

/**
 * Debug page 页面.
 */
class DebugPageFragment : BaseDebugPageFragment(), PermissionFragment.Callback {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addDebugItem("CameraHelper") {
            PermissionFragment.start(childFragmentManager, arrayListOf(Manifest.permission.CAMERA), bundleOf(
                Consts.CALLING_ID to "CameraHelper"
            ))
        }
    }

    override fun onPermissionsResult(permissions: ArrayList<String>, granted: Boolean, extraData: Bundle) {
        when (extraData.getString(Consts.CALLING_ID)) {
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
