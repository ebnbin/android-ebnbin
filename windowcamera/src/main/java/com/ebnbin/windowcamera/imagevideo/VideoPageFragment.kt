package com.ebnbin.windowcamera.imagevideo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ebnbin.windowcamera.R
import kotlinx.android.synthetic.main.video_page_fragment.*

class VideoPageFragment : BaseImageVideoPageFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.video_page_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        simple_video_view.setFile(imageVideo.file)
    }

    override fun onPause() {
        simple_video_view.onVideoPause()
        super.onPause()
    }

    override fun onDestroyView() {
        simple_video_view.release()
        super.onDestroyView()
    }
}
