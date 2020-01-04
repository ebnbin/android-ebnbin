package com.ebnbin.windowcamera.album2

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.ebnbin.windowcamera.imagevideo.ImageVideo
import java.io.File

class AlbumItem(type: Type, file: File, index: Int, var multiSelect: MultiSelect = MultiSelect.NORMAL) :
    ImageVideo(type, file, index), MultiItemEntity {
    override fun getItemType(): Int {
        return type.ordinal
    }
}
