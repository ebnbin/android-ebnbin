package com.ebnbin.windowcamera.imagevideo

import android.content.Context
import android.content.Intent
import com.ebnbin.eb2.activity.EBActivity

class ImageVideoActivity : EBActivity() {
    companion object {
        const val KEY_IMAGE_VIDEOS = "image_videos"
        const val KEY_INDEX = "index"

        fun intent(context: Context, imageVideos: ArrayList<out ImageVideo>, index: Int): Intent {
            return Intent(context, ImageVideoActivity::class.java)
                .putExtra(KEY_FRAGMENT_CLASS, ImageVideoFragment::class.java)
                .putExtra(KEY_IMAGE_VIDEOS, imageVideos)
                .putExtra(KEY_INDEX, index)
        }
    }
}
