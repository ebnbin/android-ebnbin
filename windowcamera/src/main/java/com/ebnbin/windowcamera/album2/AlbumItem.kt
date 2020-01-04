package com.ebnbin.windowcamera.album2

import android.net.Uri
import android.os.Parcelable
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.ebnbin.windowcamera.imagevideo.ImageVideo
import kotlinx.android.parcel.Parcelize

@Parcelize
class AlbumItem(val type: ImageVideo.Type, val uri: Uri, val index: Int, var multiSelect: MultiSelect = MultiSelect.NORMAL) :
    Parcelable, MultiItemEntity {
    override fun getItemType(): Int {
        return type.ordinal
    }
}
