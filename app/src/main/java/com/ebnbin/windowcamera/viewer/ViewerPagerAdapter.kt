package com.ebnbin.windowcamera.viewer

import android.content.Context
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.ebnbin.eb.fragment.instantiate

class ViewerPagerAdapter(
    private val context: Context,
    private val fm: FragmentManager,
    private val viewerItems: List<ViewerItem>
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
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

    private var lastFragment: ViewerPageFragment? = null

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        if (`object` is ViewerPageFragment && lastFragment !== `object`) {
            lastFragment?.onPageUnselected()
            lastFragment = null
            lastFragment = `object`
        }
        super.setPrimaryItem(container, position, `object`)
    }
}
