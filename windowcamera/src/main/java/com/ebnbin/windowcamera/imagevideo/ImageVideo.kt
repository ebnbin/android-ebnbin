package com.ebnbin.windowcamera.imagevideo

import java.io.File
import java.io.Serializable

open class ImageVideo(val type: Type, val file: File, val index: Int): Serializable {
    enum class Type {
        IMAGE,
        VIDEO
    }
}
