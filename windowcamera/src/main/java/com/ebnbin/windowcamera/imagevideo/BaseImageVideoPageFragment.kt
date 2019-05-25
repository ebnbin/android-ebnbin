package com.ebnbin.windowcamera.imagevideo

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import com.ebnbin.eb.fragment.EBFragment
import com.ebnbin.eb.fragment.FragmentHelper

abstract class BaseImageVideoPageFragment : EBFragment() {
    protected lateinit var imageVideo: ImageVideo
        private set

    override fun onInitArguments(savedInstanceState: Bundle?, arguments: Bundle, activityExtras: Bundle) {
        super.onInitArguments(savedInstanceState, arguments, activityExtras)
        imageVideo = arguments.getSerializable(KEY_IMAGE_VIDEO) as ImageVideo
    }

    companion object {
        const val KEY_IMAGE_VIDEO = "image_video"

        fun newInstance(fm: FragmentManager, imageVideo: ImageVideo): BaseImageVideoPageFragment {
            val fragmentClass = when (imageVideo.type) {
                ImageVideo.Type.IMAGE -> ImagePageFragment::class.java
                ImageVideo.Type.VIDEO -> VideoPageFragment::class.java
            }
            return FragmentHelper.instantiate(fm, fragmentClass, bundleOf(
                KEY_IMAGE_VIDEO to imageVideo
            ))
        }
    }
}
