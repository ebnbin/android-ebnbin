package com.ebnbin.windowcamera.album2

import android.net.Uri
import android.os.Parcelable
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.ebnbin.windowcamera.viewer.ViewerType
import kotlinx.parcelize.Parcelize

@Parcelize
class AlbumItem(val type: ViewerType, val uri: Uri, val index: Int, var multiSelect: MultiSelect = MultiSelect.NORMAL) :
    Parcelable, MultiItemEntity {
    override fun getItemType(): Int {
        return type.ordinal
    }
}
