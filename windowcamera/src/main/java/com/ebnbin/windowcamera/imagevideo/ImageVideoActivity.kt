package com.ebnbin.windowcamera.imagevideo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.ebnbin.eb.FragmentActivity

class ImageVideoActivity : FragmentActivity() {
    override val fragmentClass: Class<out Fragment>
        get() = ImageVideoFragment::class.java
    override val fragmentArguments: Bundle?
        get() = bundleOf(
            KEY_IMAGE_VIDEOS to (intent?.getSerializableExtra(KEY_IMAGE_VIDEOS) ?: throw RuntimeException()),
            KEY_INDEX to (intent?.getIntExtra(KEY_INDEX, 0) ?: 0)
        )

    companion object {
        const val KEY_IMAGE_VIDEOS = "image_videos"
        const val KEY_INDEX = "index"

        fun intent(context: Context, imageVideos: ArrayList<out ImageVideo>, index: Int): Intent {
            return Intent(context, ImageVideoActivity::class.java)
                .putExtra(KEY_IMAGE_VIDEOS, imageVideos)
                .putExtra(KEY_INDEX, index)
        }
    }
}
