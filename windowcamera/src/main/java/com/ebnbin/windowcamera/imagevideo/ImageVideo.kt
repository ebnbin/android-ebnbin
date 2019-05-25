package com.ebnbin.windowcamera.imagevideo

import java.io.Serializable

class ImageVideo(val type: Type, val url: String, val index: Int): Serializable {
    enum class Type {
        IMAGE,
        VIDEO
    }
}
