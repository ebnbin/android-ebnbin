package com.ebnbin.windowcamera.imagevideo

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ImageVideoPagerAdapter(private val fm: FragmentManager, private val imageVideos: List<ImageVideo>) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int {
        return imageVideos.size
    }

    override fun getItem(position: Int): Fragment {
        return BaseImageVideoPageFragment.newInstance(fm, imageVideos[position])
    }
}
