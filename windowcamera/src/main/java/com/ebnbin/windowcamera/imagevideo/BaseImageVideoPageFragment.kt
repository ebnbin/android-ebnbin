package com.ebnbin.windowcamera.imagevideo

import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import com.ebnbin.eb.fragment.requireArgument
import com.ebnbin.eb2.fragment.EBFragment
import com.ebnbin.eb2.fragment.FragmentHelper

abstract class BaseImageVideoPageFragment : EBFragment() {
    protected val imageVideo: ImageVideo
        get() = requireArgument(KEY_IMAGE_VIDEO)

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
