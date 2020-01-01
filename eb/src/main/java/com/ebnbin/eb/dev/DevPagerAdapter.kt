package com.ebnbin.eb.dev

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ebnbin.eb.EBApplication
import com.ebnbin.eb.fragment.instantiate

@Suppress("DEPRECATION")
internal class DevPagerAdapter(
    private val context: Context,
    private val fm: FragmentManager
) : FragmentPagerAdapter(fm, BEHAVIOR_SET_USER_VISIBLE_HINT) {
    override fun getCount(): Int {
        return EBApplication.instance.devPages.size
    }

    override fun getItem(position: Int): Fragment {
        return fm.instantiate(context, EBApplication.instance.devPages[position].first)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return EBApplication.instance.devPages[position].second
    }
}
