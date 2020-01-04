package com.ebnbin.windowcamera.viewer

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.ebnbin.eb.fragment.instantiate

@Suppress("DEPRECATION")
class ViewerPagerAdapter(
    private val context: Context,
    private val fm: FragmentManager,
    private val viewerItems: List<ViewerItem>
) : FragmentStatePagerAdapter(fm, BEHAVIOR_SET_USER_VISIBLE_HINT) {
    override fun getCount(): Int {
        return viewerItems.size
    }

    override fun getItem(position: Int): Fragment {
        val viewerItem = viewerItems[position]
        return fm.instantiate(context, when (viewerItem.type) {
            ViewerType.IMAGE -> ViewerImagePageFragment::class.java
            ViewerType.VIDEO -> ViewerVideoPageFragment::class.java
        }, ViewerPageFragment.createArguments(viewerItem))
    }
}
