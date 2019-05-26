package com.ebnbin.windowcamera.imagevideo

import com.chad.library.adapter.base.entity.MultiItemEntity
import java.io.Serializable

class ImageVideo(val type: Type, val url: String, val index: Int): MultiItemEntity, Serializable {
    override fun getItemType(): Int {
        return type.ordinal
    }

    enum class Type {
        IMAGE,
        VIDEO
    }
}
