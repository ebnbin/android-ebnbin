package com.ebnbin.eb.dev

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ebnbin.eb.fragment.instantiate

@Suppress("DEPRECATION")
internal class DevPagerAdapter(
    private val context: Context,
    private val fm: FragmentManager,
    private val devPages: List<DevPage>
) : FragmentPagerAdapter(fm, BEHAVIOR_SET_USER_VISIBLE_HINT) {
    override fun getCount(): Int {
        return devPages.size
    }

    override fun getItem(position: Int): Fragment {
        return fm.instantiate(context, devPages[position].fragmentClass).also {
            it.arguments = devPages[position].fragmentArguments
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return devPages[position].title
    }
}
