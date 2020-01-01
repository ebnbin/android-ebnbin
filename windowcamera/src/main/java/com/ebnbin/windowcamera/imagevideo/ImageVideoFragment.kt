package com.ebnbin.windowcamera.imagevideo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ebnbin.eb.fragment.getArgumentOrDefault
import com.ebnbin.eb.fragment.requireArgument
import com.ebnbin.eb2.fragment.EBFragment
import com.ebnbin.windowcamera.R
import kotlinx.android.synthetic.main.image_video_fragment.*

class ImageVideoFragment : EBFragment() {
    private val imageVideos: ArrayList<ImageVideo>
        get() = requireArgument(ImageVideoActivity.KEY_IMAGE_VIDEOS)
    private val index: Int
        get() = getArgumentOrDefault(ImageVideoActivity.KEY_INDEX, 0)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.image_video_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        val imageVideosPagerAdapter = ImageVideoPagerAdapter(childFragmentManager, imageVideos)
        view_pager.adapter = imageVideosPagerAdapter
        view_pager.setCurrentItem(index, false)
    }
}
