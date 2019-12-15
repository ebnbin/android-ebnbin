package com.ebnbin.windowcamera.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.ebnbin.eb.EBApp
import com.ebnbin.eb2.fragment.FragmentHelper
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.profile.fragment.CameraProfileFragment
import com.ebnbin.windowcamera.profile.fragment.OtherProfileFragment
import com.ebnbin.windowcamera.profile.fragment.WindowProfileFragment

class MainPagerAdapter(private val fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int {
        return ITEMS.size
    }

    override fun getItem(position: Int): Fragment {
        return FragmentHelper.instantiate(fm, ITEMS[position].first)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return ITEMS[position].second
    }

    companion object {
        val ITEMS: List<Triple<Class<out Fragment>, CharSequence, Int>> = listOf(
            Triple(WindowProfileFragment::class.java, EBApp.instance.resources.getString(R.string.window), R.drawable.profile_window),
            Triple(CameraProfileFragment::class.java, EBApp.instance.resources.getString(R.string.camera), R.drawable.profile_camera),
            Triple(OtherProfileFragment::class.java, EBApp.instance.resources.getString(R.string.other), R.drawable.profile_other)
        )
    }
}
