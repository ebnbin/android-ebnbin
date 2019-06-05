package com.ebnbin.windowcamera.camera.exception

open class CameraReportException(message: String, cause: Throwable?, private val extra: String?) :
    RuntimeException(message, cause) {
    override fun toString(): String {
        return "${super.toString()}${extra?.let { ",extra=$extra" } ?: ""}"
    }
}
