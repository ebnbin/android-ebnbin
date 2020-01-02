package com.ebnbin.eb.databinding

import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.databinding.adapters.ListenerUtil
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.ebnbin.eb.R
import com.ebnbin.eb.requireOtherViewById
import com.google.android.material.tabs.TabLayout

@BindingAdapter("isVisible")
fun setVisible(view: View, isVisible: Boolean) {
    view.isVisible = isVisible
}

@BindingAdapter("navigationOnClickListener")
fun setNavigationOnClickListener(view: Toolbar, navigationOnClickListener: View.OnClickListener?) {
    view.setNavigationOnClickListener(navigationOnClickListener)
}

@BindingAdapter("setupWithViewPager")
fun setupWithViewPager(view: TabLayout, @IdRes viewPagerId: Int) {
    if (viewPagerId == 0) {
        view.setupWithViewPager(null)
    } else {
        view.setupWithViewPager(view.requireOtherViewById(viewPagerId))
    }
}

@BindingAdapter("adapter")
fun setAdapter(view: ViewPager, adapter: PagerAdapter?) {
    view.adapter = adapter
}

@BindingAdapter("currentItem")
fun setCurrentItem(view: ViewPager, item: Int) {
    view.currentItem = item
}

@BindingAdapter("onPageScrolled", "onPageSelected", "onPageScrollStateChanged", requireAll = false)
fun setOnPageChangeListener(
    view: ViewPager,
    onPageScrolled: ViewPagerOnPageScrolled?,
    onPageSelected: ViewPagerOnPageSelected?,
    onPageScrollStateChanged: ViewPagerOnPageScrollStateChanged?
) {
    val newValue = if (onPageScrolled == null && onPageSelected == null && onPageScrollStateChanged == null) {
        null
    } else {
        object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                onPageScrolled?.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                onPageSelected?.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                onPageScrollStateChanged?.onPageScrollStateChanged(state)
            }
        }
    }
    val oldValue = ListenerUtil.trackListener(view, newValue, R.id.ebViewPagerOnPageChangeListener)
    if (oldValue != null) {
        view.removeOnPageChangeListener(oldValue)
    }
    if (newValue != null) {
        view.addOnPageChangeListener(newValue)
    }
}

interface ViewPagerOnPageScrolled {
    fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int)
}

interface ViewPagerOnPageSelected {
    fun onPageSelected(position: Int)
}

interface ViewPagerOnPageScrollStateChanged {
    fun onPageScrollStateChanged(state: Int)
}
