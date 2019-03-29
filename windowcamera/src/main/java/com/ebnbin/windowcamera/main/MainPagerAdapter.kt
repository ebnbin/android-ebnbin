package com.ebnbin.windowcamera.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ebnbin.eb.app.FragmentHelper
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.profile.CameraProfileFragment

class MainPagerAdapter(private val context: Context, private val fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val pages: List<Triple<Class<out Fragment>, CharSequence, Boolean>> =
        ArrayList<Triple<Class<out Fragment>, CharSequence, Boolean>>().apply {
            add(Triple(PlaceholderFragment::class.java, context.getString(R.string.window), false))
            add(Triple(CameraProfileFragment::class.java, context.getString(R.string.camera), false))
            add(Triple(PlaceholderFragment::class.java, context.getString(R.string.others), false))
            add(Triple(PlaceholderFragment::class.java, context.getString(R.string.album), true))
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

    /**
     * 是否在滚动时隐藏 AppBarLayout 和 BottomAppBar.
     */
    fun hideOnScroll(position: Int): Boolean {
        return pages[position].third
    }
}
