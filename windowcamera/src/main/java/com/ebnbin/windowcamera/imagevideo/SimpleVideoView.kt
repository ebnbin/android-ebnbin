package com.ebnbin.windowcamera.imagevideo

import android.content.Context
import android.util.AttributeSet
import com.shuyu.gsyvideoplayer.video.NormalGSYVideoPlayer

open class SimpleVideoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : NormalGSYVideoPlayer(context, attrs) {
    fun setUrl(url: String) {
        setUp(url, true, mTitle)
    }
}
