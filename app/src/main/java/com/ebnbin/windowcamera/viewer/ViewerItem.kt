package com.ebnbin.windowcamera.viewer

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ViewerItem(val type: ViewerType, val uri: Uri) : Parcelable
