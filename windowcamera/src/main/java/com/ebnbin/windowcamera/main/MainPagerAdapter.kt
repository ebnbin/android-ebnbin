package com.ebnbin.windowcamera.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.ebnbin.eb.fragment.FragmentHelper
import com.ebnbin.eb.util.res
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
        val ITEMS: List<Pair<Class<out Fragment>, CharSequence>> = listOf(
            Pair(WindowProfileFragment::class.java, res.getString(R.string.window)),
            Pair(CameraProfileFragment::class.java, res.getString(R.string.camera)),
            Pair(OtherProfileFragment::class.java, res.getString(R.string.other))
        )
    }
}
