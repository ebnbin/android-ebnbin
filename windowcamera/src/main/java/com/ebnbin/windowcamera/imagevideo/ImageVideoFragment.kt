package com.ebnbin.windowcamera.imagevideo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ebnbin.eb.fragment.EBFragment
import com.ebnbin.windowcamera.R
import kotlinx.android.synthetic.main.image_video_fragment.*

class ImageVideoFragment : EBFragment() {
    private lateinit var imageVideos: ArrayList<ImageVideo>
    private var index: Int = 0

    @Suppress("UNCHECKED_CAST")
    override fun onInitArguments(savedInstanceState: Bundle?, arguments: Bundle, activityExtras: Bundle) {
        super.onInitArguments(savedInstanceState, arguments, activityExtras)
        imageVideos = activityExtras.getSerializable("image_videos") as ArrayList<ImageVideo>
        index = activityExtras.getInt("index", 0)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.image_video_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageVideosPagerAdapter = ImageVideoPagerAdapter(childFragmentManager, imageVideos)
        view_pager.adapter = imageVideosPagerAdapter
        view_pager.setCurrentItem(index, false)
    }
}