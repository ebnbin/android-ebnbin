package com.ebnbin.windowcamera.imagevideo

import android.net.Uri
import android.os.Parcelable
import com.ebnbin.windowcamera.viewer.ViewerType
import kotlinx.android.parcel.Parcelize

@Parcelize
open class ImageVideo(val type: ViewerType, val uri: Uri, val index: Int): Parcelable
