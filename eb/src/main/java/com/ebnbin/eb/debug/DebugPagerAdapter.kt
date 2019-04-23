package com.ebnbin.eb.debug

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ebnbin.eb.app.FragmentHelper
import com.ebnbin.eb.util.ebApp

internal class DebugPagerAdapter(private val fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val pages: List<Pair<Class<out BaseDebugPageFragment>, CharSequence>> =
        ArrayList<Pair<Class<out BaseDebugPageFragment>, CharSequence>>().apply {
            ebApp.debugPageFragmentClass?.let {
                add(Pair(it, "Debug"))
            }
            add(Pair(EBDebugPageFragment::class.java, "Debug EB"))
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
