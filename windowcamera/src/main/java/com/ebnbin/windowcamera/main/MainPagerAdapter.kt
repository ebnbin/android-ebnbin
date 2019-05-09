package com.ebnbin.windowcamera.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.ebnbin.eb.fragment.FragmentHelper
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.profile.fragment.CameraProfileFragment
import com.ebnbin.windowcamera.profile.fragment.OtherProfileFragment
import com.ebnbin.windowcamera.profile.fragment.WindowProfileFragment

class MainPagerAdapter(
    private val context: Context,
    private val fm: FragmentManager
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val pages: List<Pair<Class<out Fragment>, CharSequence>> =
        ArrayList<Pair<Class<out Fragment>, CharSequence>>().apply {
            add(Pair(WindowProfileFragment::class.java, context.getString(R.string.window)))
            add(Pair(CameraProfileFragment::class.java, context.getString(R.string.camera)))
            add(Pair(OtherProfileFragment::class.java, context.getString(R.string.other)))
        }

    override fun getCount(): Int {
        return pages.size
    }

    override fun getItem(position: Int): Fragment {
        return FragmentHelper.instantiate(fm, pages[position].first)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return pages[position].second
    }
}
