package com.ebnbin.windowcamera.dev

import com.ebnbin.eb.dev.EBDevice
import com.ebnbin.windowcamera.camera.CameraHelper

class Device : EBDevice() {
    val cameraHelper: String = CameraHelper.toString()
}
