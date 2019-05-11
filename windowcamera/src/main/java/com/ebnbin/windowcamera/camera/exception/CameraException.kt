package com.ebnbin.windowcamera.camera.exception

open class CameraException : RuntimeException {
    val text: String

    constructor(message: String) : super(message) {
        text = message
    }

    constructor(message: String, cause: Throwable?) : super(message, cause) {
        text = message
    }

    constructor(
        message: String,
        cause: Throwable?,
        enableSuppression: Boolean,
        writableStackTrace: Boolean
    ) : super(message, cause, enableSuppression, writableStackTrace) {
        text = message
    }
}
