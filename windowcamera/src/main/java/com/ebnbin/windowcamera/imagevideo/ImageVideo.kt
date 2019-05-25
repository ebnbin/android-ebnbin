package com.ebnbin.windowcamera.imagevideo

import java.io.Serializable

class ImageVideo(val type: Type, val url: String): Serializable {
    enum class Type {
        IMAGE,
        VIDEO
    }
}
