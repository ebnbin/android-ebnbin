package com.ebnbin.windowcamera.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.ebnbin.eb.app.FragmentHelper
import com.ebnbin.windowcamera.R
import com.ebnbin.windowcamera.profile.CameraProfileFragment
import com.ebnbin.windowcamera.profile.WindowProfileFragment

class MainPagerAdapter(private val context: Context, private val fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    private val pages: List<Pair<Class<out Fragment>, CharSequence>> =
        ArrayList<Pair<Class<out Fragment>, CharSequence>>().apply {
            add(Pair(WindowProfileFragment::class.java, context.getString(R.string.window)))
            add(Pair(CameraProfileFragment::class.java, context.getString(R.string.camera)))
            add(Pair(PlaceholderFragment::class.java, context.getString(R.string.others)))
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
