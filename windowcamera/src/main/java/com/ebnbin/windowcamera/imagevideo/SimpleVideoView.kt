package com.ebnbin.windowcamera.imagevideo

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.ebnbin.windowcamera.R
import com.shuyu.gsyvideoplayer.video.NormalGSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView

open class SimpleVideoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : NormalGSYVideoPlayer(context, attrs) {
    init {
        mDismissControlTime = 2000
        mNeedShowWifiTip = false
        mIsTouchWiget = false
        mIsTouchWigetFull = false
    }

    fun setUrl(url: String) {
        setUp(url, true, mTitle)
        findViewById<ImageView>(R.id.thumb_image_view)?.let {
            Glide.with(this)
                .load(url)
                .into(it)
        }
    }

    override fun updateStartImage() {
        super.updateStartImage()
        (mStartButton as ImageView).setImageResource(when (mCurrentState) {
            GSYVideoView.CURRENT_STATE_PLAYING -> R.drawable.image_video_pause
            else -> R.drawable.image_video_play
        })
    }
}
