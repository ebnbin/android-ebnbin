package com.ebnbin.windowcamera.camera.exception

import com.ebnbin.eb.util.res
import com.ebnbin.windowcamera.R

/**
 * 在 openCamera 时权限被拒绝. 通常情况下这不应该发生.
 */
class CameraPermissionException : CameraException(res.getString(R.string.camera_error_permission))
