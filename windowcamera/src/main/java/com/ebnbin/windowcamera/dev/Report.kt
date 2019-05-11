package com.ebnbin.windowcamera.dev

import com.ebnbin.eb.dev.EBReport
import com.ebnbin.windowcamera.camera.CameraHelper

class Report : EBReport() {
    val cameraHelper: String = CameraHelper.instance.toString()
}
