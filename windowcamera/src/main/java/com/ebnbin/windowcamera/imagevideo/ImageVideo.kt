package com.ebnbin.windowcamera.imagevideo

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class ImageVideo(val type: Type, val uri: Uri, val index: Int): Parcelable {
    enum class Type {
        IMAGE,
        VIDEO
    }
}
