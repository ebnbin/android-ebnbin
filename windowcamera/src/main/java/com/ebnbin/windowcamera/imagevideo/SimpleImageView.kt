package com.ebnbin.windowcamera.imagevideo

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import com.github.chrisbanes.photoview.PhotoView

open class SimpleImageView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyle: Int = 0
) : PhotoView(context, attr, defStyle) {
    fun setUrl(url: String) {
        setImageURI(Uri.parse(url))
    }
}
