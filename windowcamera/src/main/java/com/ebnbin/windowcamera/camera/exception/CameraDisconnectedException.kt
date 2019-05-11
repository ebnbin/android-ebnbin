package com.ebnbin.windowcamera.camera.exception

import com.ebnbin.eb.util.res
import com.ebnbin.windowcamera.R

/**
 * 通常发生在通过别的应用启动相机时.
 */
class CameraDisconnectedException : CameraException(res.getString(R.string.camera_error_disconnected))
