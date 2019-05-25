package com.ebnbin.windowcamera.imagevideo

import android.content.Context
import android.content.Intent
import com.ebnbin.eb.activity.EBActivity

class ImageVideoActivity : EBActivity() {
    companion object {
        fun intent(context: Context, imageVideos: ArrayList<ImageVideo>, index: Int): Intent {
            return Intent(context, ImageVideoActivity::class.java)
                .putExtra(KEY_FRAGMENT_CLASS, ImageVideoFragment::class.java)
                .putExtra("image_videos", imageVideos)
                .putExtra("index", index)
        }
    }
}
